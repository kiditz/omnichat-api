package com.stafsus.api.controller

import com.stafsus.api.constant.UrlPath
import com.stafsus.api.dto.ResponseDto
import com.stafsus.api.dto.UserDetailDto
import com.stafsus.api.entity.UserPrincipal
import com.stafsus.api.service.ChatService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.Authentication
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(UrlPath.CHAT)
@Tag(name = "Chat", description = "Chat API")
@Validated
class ChatController(
	private val chatService: ChatService
) {

	@GetMapping("{company}")
	@Operation(summary = "Get chats", security = [SecurityRequirement(name = "bearer-key")])
	fun findChat(
		@PathVariable company: Long,
		@RequestParam page: Int,
		@RequestParam size: Int,
	): ResponseDto {
//		val user = (authentication.principal as UserDetailDto).user
		val chatPage = chatService.findChats(page, size, company)
		return ResponseDto.fromPage(chatPage)
	}

}