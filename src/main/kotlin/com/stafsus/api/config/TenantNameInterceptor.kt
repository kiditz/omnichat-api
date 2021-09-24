package com.stafsus.api.config

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
@Component
class TenantNameInterceptor : HandlerInterceptor {
//	override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
//		val tenantId = request.getHeader("X-CompanyID")
//		ThreadLocalStorage.setTenant(tenantId?.toLong())
//		return true
//	}
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