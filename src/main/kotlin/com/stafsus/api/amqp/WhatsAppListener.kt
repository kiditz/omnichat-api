package com.stafsus.api.amqp

import com.stafsus.api.config.AmqpConfig
import com.stafsus.api.dto.WhatsAppChannelDto
import com.stafsus.api.service.WhatsAppChannelService
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class WhatsAppListener(
	private val whatsAppChannelService: WhatsAppChannelService
) {
	private val log = LoggerFactory.getLogger(javaClass)

	@RabbitListener(queues = [AmqpConfig.WA_QR_Q])
	fun whatsAppQr(channelDto: WhatsAppChannelDto) {
		log.info("Qr : {}", channelDto)
		channelDto.isActive = true
		channelDto.isPending = false
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
		channelDto.isOnline = true
		channelDto.qrCode = null
		whatsAppChannelService.save(channelDto)
	}
}