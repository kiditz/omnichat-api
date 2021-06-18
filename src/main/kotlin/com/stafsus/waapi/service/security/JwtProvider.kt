package com.stafsus.waapi.service.security

import com.stafsus.waapi.service.dto.TokenDto
import com.stafsus.waapi.service.dto.UserPrincipal
import org.springframework.security.core.Authentication

interface JwtProvider {
    fun generateToken(userPrincipal: UserPrincipal): TokenDto
    fun generateToken(authentication: Authentication): TokenDto
    fun getUserIdFromToken(token: String?): Long?
    fun validateToken(authToken: String?): Boolean
    fun generateRefreshToken(id: Long): String
}