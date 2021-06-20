package com.stafsus.waapi.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class HealthController {
    @GetMapping("/health")
    @Operation(security = [SecurityRequirement(name = "bearer-key")])
    fun index(): Map<String, Any> {
        return mapOf(
            "name" to "Wa Api",
            "description" to "Api To Access By Stafsus",
        )
    }
}