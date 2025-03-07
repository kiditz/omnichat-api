package com.stafsus.api.amqp

import com.stafsus.api.config.AmqpConfig
import com.stafsus.api.dto.WaSyncChatDto
import com.stafsus.api.dto.WhatsAppChannelDto
import com.stafsus.api.dto.WaSyncContactDto
import com.stafsus.api.dto.WaSyncMessageDto
import com.stafsus.api.service.ChatService
import com.stafsus.api.service.ContactService
import com.stafsus.api.service.MessageService
import com.stafsus.api.service.WhatsAppChannelService
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class WhatsAppListener(
	private val whatsAppChannelService: WhatsAppChannelService,
	private val contactService: ContactService,
	private val chatService: ChatService,
	private val messageService: MessageService,
) {
	private val log = LoggerFactory.getLogger(javaClass)

	@RabbitListener(queues = [AmqpConfig.WA_QR_Q])
	fun whatsAppQr(channelDto: WhatsAppChannelDto) {
		log.info("Qr : {}", channelDto)
		whatsAppChannelService.save(channelDto)
	}

	@RabbitListener(queues = [AmqpConfig.WA_READY_Q])
	fun whatsAppReady(channelDto: WhatsAppChannelDto) {
		log.info("Ready : {}", channelDto)
		whatsAppChannelService.save(channelDto)
	}

	@RabbitListener(queues = [AmqpConfig.WA_AUTHENTICATION_Q])
	fun whatsAppAuthenticated(channelDto: WhatsAppChannelDto) {
		log.info("Authenticated : {}", channelDto)
		whatsAppChannelService.save(channelDto)
	}

	@RabbitListener(queues = [AmqpConfig.WA_DISCONNECT_Q, AmqpConfig.WA_AUTH_FAILURE_Q])
	fun whatsAppExit(channelDto: WhatsAppChannelDto) {
		log.info("Disconnect : {}", channelDto)
		channelDto.phone = ""
		channelDto.qrCode = ""
		channelDto.browserSession = ""
		whatsAppChannelService.save(channelDto)
	}

	@RabbitListener(queues = [AmqpConfig.WA_SYNC_CONTACT_Q])
	fun whatsAppSyncContact(contactDto: WaSyncContactDto) {
		log.info("Sync Contact : {}", contactDto.deviceId)
		contactService.syncFromWhatsApp(contactDto)
	}


	@RabbitListener(queues = [AmqpConfig.WA_SYNC_MESSAGE_Q])
	fun whatsAppSyncMessage(messageDto: WaSyncMessageDto) {
		log.info("Sync Message : {}", messageDto.message.from)
		messageService.syncFromWhatsApp(messageDto)
	}


	@RabbitListener(queues = [AmqpConfig.WA_SYNC_MESSAGE_REVOKED_Q])
	fun whatsAppSyncRevokedMessage(messageDto: WaSyncMessageDto) {
		log.info("Revoked Message : {}", messageDto.message.from)
		messageService.revokedFromWhatsApp(messageDto)
	}


	@RabbitListener(queues = [AmqpConfig.WA_SYNC_CHAT_Q])
	fun whatsAppSyncChatList(chatDto: WaSyncChatDto) {
		log.info("Sync Chat : {}", chatDto.deviceId)
		chatService.syncFromWhatsApp(chatDto)
	}
}