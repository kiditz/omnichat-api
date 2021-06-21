package com.stafsus.waapi.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController {
    @GetMapping("/")
    fun index(): Map<String, Any> {
        return mapOf(
            "name" to "Wa Api",
            "description" to "Api To Access By Stafsus",
        )
    }
}