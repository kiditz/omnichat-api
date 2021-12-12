package com.stafsus.api.controller

import com.stafsus.api.constant.UrlPath
import com.stafsus.api.dto.ResponseDto
import com.stafsus.api.service.ApiKeyService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(UrlPath.API_KEY)
@Tag(name = "Api Key", description = "Api Key API")
class ApiKeyController(
	private val apiKeyService: ApiKeyService
) {
	@GetMapping
	@Operation(summary = "Get api key data", security = [SecurityRequirement(name = "bearer-key")])
	fun findApiKey(): ResponseDto {
		val apiKey = apiKeyService.findApiKey()
		return ResponseDto(payload = apiKey)
	}


}