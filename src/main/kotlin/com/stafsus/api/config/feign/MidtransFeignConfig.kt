package com.stafsus.api.config.feign

import feign.auth.BasicAuthRequestInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class MidtransFeignConfig(
	@Value("\${midtrans.serverKey}") val serverKey: String
) {
	@Bean
	fun basicAuthRequestInterceptor(): BasicAuthRequestInterceptor? {
		return BasicAuthRequestInterceptor(serverKey, "")
	}
}