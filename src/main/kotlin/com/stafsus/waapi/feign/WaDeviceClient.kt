package com.stafsus.waapi.feign

import com.stafsus.waapi.config.FeignConfig
import com.stafsus.waapi.service.dto.ResponseDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import java.net.URI


@FeignClient(value = "wa-device", url = "wa-device", configuration = [FeignConfig::class])
interface WaDeviceClient {
    @GetMapping("/api/qr")
    fun getQrCode(baseUri: URI): ResponseDto
}