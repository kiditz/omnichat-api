package com.stafsus.waapi.api

import com.stafsus.waapi.service.WhatsMessageService
import com.stafsus.waapi.service.dto.ResponseDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/wa")
@Validated
@Tag(name = "Chat Api", description = "Call whats app api")
class WaMessageController(private val whatsMessageService: WhatsMessageService) {
	@Operation(security = [SecurityRequirement(name = "bearer-key")], summary = "Get whats app contact list")
	@GetMapping("/contacts")
	fun getContacts(@RequestParam deviceId: String): ResponseDto {

		return whatsMessageService.getContacts(deviceId)
	}

	@Operation(security = [SecurityRequirement(name = "bearer-key")], summary = "Get whats app chat list")
	@GetMapping("/chats")
	fun getChat(@RequestParam deviceId: String): ResponseDto {
		return whatsMessageService.getChat(deviceId)
	}

	@Operation(security = [SecurityRequirement(name = "bearer-key")], summary = "Download whats app media")
	@GetMapping("/media")
	fun downloadMedia(
		@RequestParam deviceId: String,
		@RequestParam chatId: String,
		@RequestParam messageId: String
	): ResponseDto {
		return whatsMessageService.downloadMedia(deviceId, chatId, messageId)
	}

	@Operation(
		security = [SecurityRequirement(name = "bearer-key")],
		summary = "Get detail or chats from whats app web"
	)
	@GetMapping("/chats/{chatId}")
	fun getChatById(@RequestParam deviceId: String, @PathVariable chatId: String): ResponseDto {
		return whatsMessageService.getChatDetail(deviceId, chatId)
	}
}