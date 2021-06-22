package com.stafsus.waapi.config.interceptor

import com.stafsus.waapi.service.security.JwtAuthenticationFilter
import com.stafsus.waapi.service.security.JwtProvider
import com.stafsus.waapi.utils.Random
import org.apache.commons.lang3.StringUtils
import org.apache.logging.log4j.ThreadContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.HandlerMethod
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class LogInterceptor(
    private val jwtProvider: JwtProvider
) : HandlerInterceptor {
    private val tracingId: String = "tracingId"
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        ThreadContext.put(tracingId, Random.string(5))
        var userId: Long? = null
        val token = JwtAuthenticationFilter.getJwtFromRequest(request)
        var controllerName = ""
        var actionName = ""
        if (StringUtils.isNotEmpty(token)) {
            userId = jwtProvider.getUserIdFromToken(token)
        }
        if (handler is HandlerMethod) {
            // there are cases where this handler isn't an instance of HandlerMethod, so the cast fails.
            val handlerMethod: HandlerMethod = handler
            controllerName = handlerMethod.bean.javaClass.simpleName
            actionName = handlerMethod.method.name
        }
        log.info("Start request with for ${ThreadContext.get("tracingId")} $controllerName $actionName  IP: ${request.remoteAddr} User: $userId")
        return super.preHandle(request, response, handler)
    }

    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?
    ) {

        log.info("Process request ${ThreadContext.get(tracingId)}")
        super.postHandle(request, response, handler, modelAndView)
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        log.info("End request ${ThreadContext.get(tracingId)}")
        ThreadContext.remove(tracingId)
        super.afterCompletion(request, response, handler, ex)
    }
}