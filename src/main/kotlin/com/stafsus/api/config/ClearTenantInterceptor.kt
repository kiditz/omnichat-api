package com.stafsus.api.config

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class ClearTenantInterceptor : HandlerInterceptor {
	private val log = LoggerFactory.getLogger(javaClass)
	override fun afterCompletion(
		request: HttpServletRequest,
		response: HttpServletResponse,
		handler: Any,
		ex: Exception?
	) {
		log.info("Reset Thread Local: {} to null", ThreadLocalStorage.getTenantId())
		ThreadLocalStorage.setTenant(null)
	}

}