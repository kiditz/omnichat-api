package com.stafsus.waapi.service.security

import com.stafsus.waapi.constant.MessageKey
import com.stafsus.waapi.exception.ValidationException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JwtAuthenticationFilter(
	private val jwtProvider: JwtProvider,
	private val securityUserService: SecurityUserService
) : OncePerRequestFilter() {
	override fun doFilterInternal(
		request: HttpServletRequest,
		response: HttpServletResponse,
		filterChain: FilterChain
	) {
		val token = getJwtFromRequest(request)
		if (StringUtils.hasText(token) && jwtProvider.validateToken(token) && !securityUserService.isBlocked(token!!)) {
			val userId = jwtProvider.getUserIdFromToken(token)
			if (userId != null) {
				val userDetails = securityUserService.loadUserId(userId)
					.orElseThrow { ValidationException(MessageKey.USER_NOT_FOUND) }
				val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
				authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
				SecurityContextHolder.getContext().authentication = authentication
			}

		}
		filterChain.doFilter(request, response)
	}

	companion object {
		fun getJwtFromRequest(request: HttpServletRequest): String? {
			val bearerToken = request.getHeader("Authorization")
			return if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
				bearerToken.substring(7, bearerToken.length)
			} else null
		}
	}
}