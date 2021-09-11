package com.stafsus.api.client

import com.stafsus.api.config.feign.MidtransFeignConfig
import com.stafsus.api.dto.midtrans.MidtransRequest
import com.stafsus.api.dto.midtrans.MidtransResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "midtrans", url = "\${midtrans.baseUrl}", configuration = [MidtransFeignConfig::class])
interface MidtransClient {
	@PostMapping("/transactions")
	fun buyPackage(midtransRequest: MidtransRequest): MidtransResponse
}