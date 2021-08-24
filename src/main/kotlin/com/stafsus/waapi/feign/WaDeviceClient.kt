package com.stafsus.waapi.feign

import com.stafsus.waapi.config.FeignConfig
import com.stafsus.waapi.service.dto.ResponseDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import java.net.URI


@FeignClient(value = "wa-device", url = "wa-device", configuration = [FeignConfig::class])
interface WaDeviceClient {
	@GetMapping("/api/contacts")
	fun getContacts(baseUri: URI): ResponseDto

	@GetMapping("/api/chats")
	fun getChat(baseUri: URI): ResponseDto

	@GetMapping("/api/chats/{id}")
	fun getChatDetail(baseUri: URI, @PathVariable id: String): ResponseDto

	@GetMapping("/api/media")
	fun downloadMedia(baseUri: URI, @RequestParam chatId: String, @RequestParam messageId: String): ResponseDto

	@DeleteMapping("/api/logout")
	fun logout(baseUri: URI): ResponseDto
}