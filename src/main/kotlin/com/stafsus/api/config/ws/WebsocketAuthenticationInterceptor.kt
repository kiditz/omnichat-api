package com.stafsus.api.config.ws

import com.stafsus.api.dto.UserDetailDto
import com.stafsus.api.service.JwtService
import com.stafsus.api.service.UserService
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.stereotype.Service

@Service
class WebsocketAuthenticationInterceptor(
	private val jwtService: JwtService,
	private val userService: UserService
) : ChannelInterceptor {
	override fun preSend(message: Message<*>, channel: MessageChannel): Message<*> {
		val accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java)
		if (StompCommand.CONNECT == accessor!!.command) {
			val authorization = accessor.getNativeHeader("Authorization")
			val accessToken: String = authorization!![0].split(" ")[1]
			val userId = jwtService.getUserIdFromToken(accessToken)
			val userDetails = userService.loadUserById(userId!!) as UserDetailDto
			accessor.user = userDetails
		}
		return message
	}
}