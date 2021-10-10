package com.stafsus.api.service

import com.stafsus.api.config.AmqpConfig
import com.stafsus.api.dto.MailMessageDto
import com.stafsus.api.entity.Channel
import com.stafsus.api.entity.ProductType
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class RabbitServiceImpl(
	private val rabbitTemplate: RabbitTemplate
) : RabbitService {
	private val log = LoggerFactory.getLogger(javaClass)
	override fun sendInstall(channel: Channel) {
		rabbitTemplate.convertAndSend(
			"${lower(channel.product!!.type!!)}_ex",
			"${lower(channel.product!!.type!!)}_rk",
			channel
		)
	}

	override fun sendRestart(channel: Channel) {
		log.info("Restart: {}", "${lower(channel.product!!.type!!)}_restart")
		rabbitTemplate.convertAndSend(
			"${lower(channel.product!!.type!!)}_restart_ex",
			"${lower(channel.product!!.type!!)}_restart_rk",
			channel
		)
	}


	override fun sendStop(channel: Channel) {
		log.info("Stop: {}", "${lower(channel.product!!.type!!)}_stop")
		rabbitTemplate.convertAndSend(
			"${lower(channel.product!!.type!!)}_stop_ex",
			"${lower(channel.product!!.type!!)}_stop_rk",
			channel
		)
	}

	override fun sendEmail(mailMessageDto: MailMessageDto) {
		rabbitTemplate.convertAndSend("${AmqpConfig.SEND_EMAIL}_q", mailMessageDto)
	}

	private fun lower(productType: ProductType): String {
		return StringUtils.lowerCase(productType.name)
	}
}