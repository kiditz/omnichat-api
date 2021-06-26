package com.stafsus.waapi.api

import io.swagger.v3.oas.annotations.Hidden
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
@Hidden
class HealthController {

	@GetMapping("/health")
	fun index(): Map<String, Any> {
		return mapOf(
			"name" to "Wa Api",
			"description" to "Api To Access By Stafsus",
		)
	}
}