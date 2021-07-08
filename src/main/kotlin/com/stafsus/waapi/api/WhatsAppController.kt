package com.stafsus.waapi.api

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/wa")
@Validated
@Tag(name = "Message", description = "Control your whats app web device")
class WhatsAppController {
}