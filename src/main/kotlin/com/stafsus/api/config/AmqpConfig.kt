package com.stafsus.api.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.stafsus.api.entity.ProductType.TELEGRAM_BOT
import com.stafsus.api.entity.ProductType.UNOFFICIAL_WHATSAPP
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
class AmqpConfig(
	private val objectMapper: ObjectMapper
) {
	companion object {
		const val WA_SYNC_CONTACT_Q = "wa_sync_contact_q"
		const val WA_SYNC_MESSAGE_Q = "wa_sync_message_q"
		const val WA_READY_Q = "wa_ready_q"
		const val WA_QR_Q = "wa_qr_q"
		const val WA_AUTHENTICATION_Q = "wa_authentication_q"
		const val WA_DISCONNECT_Q = "wa_disconnect_q"
		const val WA_AUTH_FAILURE_Q = "wa_auth_failure_q"
		const val WA_SYNC_CHAT_Q = "wa_sync_chat_q"
		const val WA_SYNC_MESSAGE_REVOKED_Q = "wa_sync_message_revoked_q"
		const val SEND_EMAIL = "send_email"
	}

	@Bean
	fun sendEmailApp(): Queue {
		return Queue("${SEND_EMAIL}_q", true)
	}


	@Bean
	fun sendEmailAppExchange(): DirectExchange {
		return DirectExchange("${SEND_EMAIL}_ex")
	}

	@Bean
	fun sendEmailBinding(): Binding {
		return BindingBuilder.bind(sendEmailApp()).to(sendEmailAppExchange())
			.withQueueName()
	}

	@Bean
	fun installUnofficialWhatsApp(): Queue {
		return Queue("${UNOFFICIAL_WHATSAPP.name}_q".lowercase(), true)
	}

	@Bean
	fun installUnofficialWhatsAppExchange(): DirectExchange {
		return DirectExchange("${UNOFFICIAL_WHATSAPP.name}_ex".lowercase())
	}

	@Bean
	fun bindUnofficialWhatsApp(): Binding {
		return BindingBuilder.bind(installUnofficialWhatsApp()).to(installUnofficialWhatsAppExchange())
			.with("${UNOFFICIAL_WHATSAPP.name}_rk".lowercase())
	}

	@Bean
	fun installTelegram(): Queue {
		return Queue("${TELEGRAM_BOT.name}_q".lowercase(), true)
	}

	@Bean
	fun installTelegramAppExchange(): DirectExchange {
		return DirectExchange("${TELEGRAM_BOT.name}_ex".lowercase())
	}

	@Bean
	fun bindTelegram(): Binding {
		return BindingBuilder.bind(installUnofficialWhatsApp()).to(installUnofficialWhatsAppExchange())
			.with("${TELEGRAM_BOT.name}_rk".lowercase())
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
		val mapper = objectMapper.findAndRegisterModules()
		return Jackson2JsonMessageConverter(mapper)
	}
}