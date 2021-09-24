package com.stafsus.api.service

import com.stafsus.api.constant.MessageKey
import com.stafsus.api.dto.WaSyncMessageDto
import com.stafsus.api.entity.Channel
import com.stafsus.api.entity.Chat
import com.stafsus.api.entity.Contact
import com.stafsus.api.entity.Message
import com.stafsus.api.exception.ValidationException
import com.stafsus.api.repository.ChannelRepository
import com.stafsus.api.repository.ChatRepository
import com.stafsus.api.repository.ContactRepository
import com.stafsus.api.repository.MessageRepository
import org.slf4j.LoggerFactory
import org.springframework.amqp.AmqpRejectAndDontRequeueException
import org.springframework.stereotype.Service

@Service
class MessageServiceImpl(
	private val messageRepository: MessageRepository,
	private val chatRepository: ChatRepository,
	private val contactRepository: ContactRepository,
	private val channelRepository: ChannelRepository
) : MessageService {
	private val log = LoggerFactory.getLogger(javaClass)
	override fun syncFromWhatsApp(waMessageDto: WaSyncMessageDto) {
		try {
			val channel = channelRepository.findByDeviceId(waMessageDto.deviceId)
				.orElseThrow { ValidationException(MessageKey.SYNC_CHAT_FAILED) }
			val message = waMessageDto.message
			val chat = waMessageDto.message.chat
			val contact = waMessageDto.contact
			saveContact(contact, channel)
			saveChat(chat!!, channel, message)
		} catch (ex: Exception) {
			log.error("Exception: {}", ex.message)
			throw AmqpRejectAndDontRequeueException(ex.message)
		}
	}

	override fun revokedFromWhatsApp(waMessageDto: WaSyncMessageDto) {
		try {
			val channel = channelRepository.findByDeviceId(waMessageDto.deviceId)
				.orElseThrow { ValidationException(MessageKey.SYNC_CHAT_FAILED) }
			val message =
				messageRepository.findByFromAndTimestamp(waMessageDto.message.from, waMessageDto.message.timestamp)
			message.ifPresent {
				waMessageDto.message.version = it.version
				waMessageDto.message.id = it.id
				waMessageDto.message.chat?.version = it.chat!!.version
				waMessageDto.message.chat?.company = channel.company
				messageRepository.save(waMessageDto.message)
			}
		} catch (ex: Exception) {
			log.error("Exception: {}", ex.message)
			throw AmqpRejectAndDontRequeueException(ex.message)
		}

	}

	private fun saveChat(
		chat: Chat,
		channel: Channel,
		message: Message
	) {
		chat?.company = channel.company
		val existingChat = chatRepository.findById(chat?.id!!)
		existingChat.ifPresent {
			chat.version = it.version
		}
		message.chat = chat
		messageRepository.save(message)
	}

	private fun saveContact(contact: Contact?, channel: Channel) {
		if (contact != null) {
			contact.company = channel.company
			val existingContact = contactRepository.findById(contact.id!!)
			existingContact.ifPresent {
				contact.version = it.version
			}
			contactRepository.save(contact)
		}
	}

}