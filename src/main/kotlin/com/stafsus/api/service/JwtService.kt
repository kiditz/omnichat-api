package com.stafsus.api.service

import com.stafsus.api.dto.AccessTokenDto
import com.stafsus.api.entity.UserPrincipal
import org.springframework.security.core.Authentication

interface JwtService {
	fun generate(userPrincipal: UserPrincipal): AccessTokenDto
	fun generate(authentication: Authentication): AccessTokenDto
	fun generateRefreshToken(id: Long): String
	fun getUserIdFromToken(token: String?): Long?
	fun validateToken(authToken: String?): Boolean
}