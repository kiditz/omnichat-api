package com.stafsus.api.config

import com.stafsus.api.service.JwtService
import com.stafsus.api.service.UserService
import org.apache.commons.lang3.StringUtils
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationFilter(
	private val userService: UserService,
	private val jwtService: JwtService
) : OncePerRequestFilter() {
	override fun doFilterInternal(
		request: HttpServletRequest,
		response: HttpServletResponse,
		filterChain: FilterChain
	) {
		val token = getJwtFromRequest(request)
		if (StringUtils.isNotEmpty(token) && jwtService.validateToken(token)) {
			val userId = jwtService.getUserIdFromToken(token)
			if (userId != null) {
				val userDetails = userService.loadUserById(userId)
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
			return if (StringUtils.isNotEmpty(bearerToken) && bearerToken.startsWith("Bearer ")) {
				bearerToken.substring(7, bearerToken.length)
			} else null
		}
	}

}
