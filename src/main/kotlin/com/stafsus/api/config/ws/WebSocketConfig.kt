package com.stafsus.api.config.ws

import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
class WebSocketConfig(
	private val websocketAuthenticationInterceptor: WebsocketAuthenticationInterceptor
) : WebSocketMessageBrokerConfigurer {
	override fun configureMessageBroker(registry: MessageBrokerRegistry) {
		registry.enableSimpleBroker("/topic")
		registry.setApplicationDestinationPrefixes("/app")
		registry.setUserDestinationPrefix("/user")
	}

	override fun registerStompEndpoints(registry: StompEndpointRegistry) {
		registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS()
	}

	override fun configureClientInboundChannel(registration: ChannelRegistration) {
		registration.interceptors(websocketAuthenticationInterceptor)
	}

}