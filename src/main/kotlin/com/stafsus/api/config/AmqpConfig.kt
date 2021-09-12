package com.stafsus.api.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@EnableRabbit
class AmqpConfig {
	companion object {
		const val UNOFFICIAL_WHATSAPP = "unofficial_whatsapp"
		const val UPDATE_CHANNEL = "update_channel"
		const val WA_SYNC_CONTACT_Q = "wa_sync_contact_q"
		const val WA_READY_Q = "wa_ready_q"
		const val WA_QR_Q = "wa_qr_q"
		const val WA_AUTHENTICATION_Q = "wa_authentication_q"
	}


	@Bean
	fun installUnofficialWhatsApp(): Queue {
		return Queue("${UNOFFICIAL_WHATSAPP}_q", true)
	}

	@Bean
	fun installUnofficialWhatsAppExchange(): DirectExchange {
		return DirectExchange("${UNOFFICIAL_WHATSAPP}_ex")
	}

	@Bean
	fun bindUnofficialWhatsApp(): Binding {
		return BindingBuilder.bind(installUnofficialWhatsApp()).to(installUnofficialWhatsAppExchange())
			.with("${UNOFFICIAL_WHATSAPP}_rk")
	}


	@Bean
	fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
		val rabbitTemplate = RabbitTemplate(connectionFactory)
		rabbitTemplate.messageConverter = producerJackson2MessageConverter()
		return rabbitTemplate
	}

	@Bean
	fun rabbitAdmin(connectionFactory: ConnectionFactory): AmqpAdmin {
		return RabbitAdmin(connectionFactory)
	}

	@Bean
	fun producerJackson2MessageConverter(): Jackson2JsonMessageConverter {
		val mapper = ObjectMapper().findAndRegisterModules()
		return Jackson2JsonMessageConverter(mapper)
	}
}