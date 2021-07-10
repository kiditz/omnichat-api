package com.stafsus.waapi.api

import com.stafsus.waapi.service.DeviceApiService
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
class DeviceApiController(private val deviceApiService: DeviceApiService) {
	@Operation(security = [SecurityRequirement(name = "bearer-key")], summary = "Get whats app contact list")
	@GetMapping("/contacts")
	fun getContacts(@RequestParam deviceId: String): ResponseDto {
		return deviceApiService.getContacts(deviceId)
	}

	@Operation(security = [SecurityRequirement(name = "bearer-key")], summary = "Get whats contact chat")
	@GetMapping("/chats")
	fun getChat(@RequestParam deviceId: String): ResponseDto {
		return deviceApiService.getChat(deviceId)
	}
}