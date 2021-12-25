package com.stafsus.api.service

import com.stafsus.api.constant.MessageKey
import com.stafsus.api.dto.WaSyncChatDto
import com.stafsus.api.exception.ValidationException
import com.stafsus.api.projection.ChatProjection
import com.stafsus.api.repository.ChannelRepository
import com.stafsus.api.repository.ChatRepository
import org.slf4j.LoggerFactory
import org.springframework.amqp.AmqpRejectAndDontRequeueException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChatServiceImpl(
	private val chatRepository: ChatRepository,
	private val channelRepository: ChannelRepository,
	private val companyService: CompanyService,
) : ChatService {
	private val log = LoggerFactory.getLogger(javaClass)

	@Transactional
	override fun syncFromWhatsApp(waChatDto: WaSyncChatDto) {
		try {
			val channel = channelRepository.findByDeviceId(waChatDto.deviceId!!)
				.orElseThrow { ValidationException(MessageKey.SYNC_CHAT_FAILED) }
			log.info("Incoming : {}", waChatDto.chats.size)
			val idsOnly = waChatDto.chats.map { it.id }.toList()
			val existingChatIds = chatRepository.findByIdIn(idsOnly)
			val chats = waChatDto.chats.filterNot { x -> existingChatIds.any { m -> m.id == x.id } }
			chats.forEach { chat ->
				chat.company = channel.company
				chat.groupMetadata?.participants?.forEach { participant ->
					participant.groupMetadata = chat.groupMetadata
				}
				chat.messages?.forEach { message ->
					message.chat = chat
				}
			}
			chatRepository.saveAll(chats)
		} catch (ex: Exception) {
			log.error("Exception: {}", ex)
			throw AmqpRejectAndDontRequeueException(ex.message)
		}
	}

	override fun findChats(page: Int, size: Int): Page<ChatProjection> {
		val company = companyService.getCompany()
		return chatRepository.findByCompanyId(
			company.id!!,
			PageRequest.of(page, size).withSort(Sort.by("timestamp").descending())
		)
	}


}