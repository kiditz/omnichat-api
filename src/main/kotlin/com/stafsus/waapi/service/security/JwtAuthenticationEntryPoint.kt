package com.stafsus.waapi.service.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.stafsus.waapi.constant.MessageKey
import com.stafsus.waapi.service.TranslateService
import com.stafsus.waapi.service.dto.ResponseDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationEntryPoint(
    private val mapper: ObjectMapper,
    private val translateService: TranslateService
) : AuthenticationEntryPoint {
    val log: Logger = LoggerFactory.getLogger(javaClass)
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse,
        authException: AuthenticationException?
    ) {
        log.error("Unauthorized - {}", authException!!.message)
        response.contentType = "application/json;charset=UTF-8"
        response.status = HttpStatus.UNAUTHORIZED.value()
        val status = ResponseDto(false, translateService.toLocale(MessageKey.UNAUTHORIZED_ERROR), null)
        response.writer.write(
            mapper.writeValueAsString(
                status
            )
        )
    }
}