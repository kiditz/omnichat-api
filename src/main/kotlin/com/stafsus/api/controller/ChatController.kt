package com.stafsus.api.controller

import com.stafsus.api.constant.UrlPath
import com.stafsus.api.dto.ResponseDto
import com.stafsus.api.service.ChatService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(UrlPath.CHAT)
@Tag(name = "Chat", description = "Chat API")
@Validated
class ChatController(
	private val chatService: ChatService
) {

	@GetMapping
	@Operation(summary = "Get chats", security = [SecurityRequirement(name = "bearer-key")])
	fun findChat(
		@RequestParam page: Int,
		@RequestParam size: Int,
	): ResponseDto {
		val chatPage = chatService.findChats(page, size)
		return ResponseDto.fromPage(chatPage)
	}

}