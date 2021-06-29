package com.stafsus.waapi.config

import com.stafsus.waapi.constant.MessageKey
import com.stafsus.waapi.exception.ValidationException
import com.stafsus.waapi.service.security.JwtProvider
import com.stafsus.waapi.service.security.SecurityUserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer


@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig(
	private val jwtProvider: JwtProvider,
	private val securityUserService: SecurityUserService,

	) : WebSocketMessageBrokerConfigurer {
	val log: Logger = LoggerFactory.getLogger(javaClass)


	override fun configureMessageBroker(registry: MessageBrokerRegistry) {
		registry.enableSimpleBroker("/topic", "/user")
		registry.setApplicationDestinationPrefixes("/app")
		registry.setUserDestinationPrefix("/user")
	}

	override fun registerStompEndpoints(registry: StompEndpointRegistry) {
		registry.addEndpoint("/ws").setAllowedOriginPatterns("*")
	}

	override fun configureClientInboundChannel(registration: ChannelRegistration) {
		registration.interceptors(object : ChannelInterceptor {
			override fun preSend(message: Message<*>, channel: MessageChannel): Message<*> {
				val accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java)
				if (StompCommand.CONNECT == accessor?.command) {
					val token = accessor.getFirstNativeHeader("X-Authorization")
					val userId = jwtProvider.getUserIdFromToken(token)
					log.info("User :{}", userId)
					val user = securityUserService.loadUserId(userId!!).orElseThrow {
						ValidationException(
							MessageKey.USER_NOT_FOUND
						)
					}

					accessor.user = user
					log.info("Access Token : {}", token)
				}
				return message
			}
		})
	}
}