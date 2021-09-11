package com.stafsus.api.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.stafsus.api.dto.ResponseDto
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomAccessDeniedHandler(
	private val mapper: ObjectMapper
) : AccessDeniedHandler {
	@Throws(IOException::class, ServletException::class)
	override fun handle(
		request: HttpServletRequest,
		response: HttpServletResponse,
		accessDeniedException: AccessDeniedException
	) {
		response.contentType = "application/json;charset=UTF-8"
		response.status = 403
		val status = ResponseDto(false, accessDeniedException.message!!, key = "access.denied")
		response.writer.write(
			mapper.writeValueAsString(
				status
			)
		)
	}
}