package com.stafsus.api.service

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
	override fun sendInstall(productType: ProductType, channel: Channel) {
		rabbitTemplate.convertAndSend("${lower(productType)}_ex", "${lower(productType)}_rk", channel)
	}

	override fun sendRestart(productType: ProductType, channel: Channel) {
		log.info("Restart: {}", "${lower(productType)}_restart")
		rabbitTemplate.convertAndSend("${lower(productType)}_restart_ex", "${lower(productType)}_restart_rk", channel)
	}

	private fun lower(productType: ProductType): String {
		return StringUtils.lowerCase(productType.name)
	}
}