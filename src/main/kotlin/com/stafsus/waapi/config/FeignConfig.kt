package com.stafsus.waapi.config

import feign.Feign
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope


@Configuration
class FeignConfig {
    @Bean
    @Scope("prototype")
    fun feignBuilder(): Feign.Builder? {
        return Feign.builder()
    }
}