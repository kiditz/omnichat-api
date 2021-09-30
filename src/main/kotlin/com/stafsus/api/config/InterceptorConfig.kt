package com.stafsus.api.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class InterceptorConfig(
	val tenantNameInterceptor: TenantNameInterceptor
): WebMvcConfigurer {
	override fun addCorsMappings(registry: CorsRegistry) {
		registry.addMapping("/**").allowedMethods("*")
	}
	override fun addInterceptors(registry: InterceptorRegistry) {
		registry.addInterceptor(tenantNameInterceptor)
	}
}