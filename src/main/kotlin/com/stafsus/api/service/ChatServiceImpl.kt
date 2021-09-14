package com.stafsus.api.service

import com.stafsus.api.constant.MessageKey
import com.stafsus.api.dto.WaSyncChatDto
import com.stafsus.api.execption.ValidationException
import com.stafsus.api.repository.ChannelRepository
import com.stafsus.api.repository.ChatRepository
import org.slf4j.LoggerFactory
import org.springframework.amqp.AmqpRejectAndDontRequeueException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChatServiceImpl(
	private val chatRepository: ChatRepository,
	private val channelRepository: ChannelRepository
) : ChatService {
	private val log = LoggerFactory.getLogger(javaClass)

	@Transactional
	override fun syncFromWhatsApp(waChatDto: WaSyncChatDto) {
		try {
			val channel = channelRepository.findByDeviceId(waChatDto.deviceId!!)
				.orElseThrow { ValidationException(MessageKey.SYNC_CHAT_FAILED) }
			log.info("Incoming : {}", waChatDto.chats.size)
			val idsOnly = waChatDto.chats.map { it.id }.toList()
			val existingContactsIds = chatRepository.findByIdIn(idsOnly)
			val chats = waChatDto.chats.filterNot { x -> existingContactsIds.any { m -> m.id == x.id } }
			chats.forEach { chat ->
				chat.user = channel.user
				chat.groupMetadata?.participants?.forEach { participant ->
					participant.groupMetadata = chat.groupMetadata
				}
			}
			chatRepository.saveAll(chats)
		} catch (ex: Exception) {
			log.error("Exception: {}", ex.message)
			throw AmqpRejectAndDontRequeueException(ex.message)
		}
	}
}