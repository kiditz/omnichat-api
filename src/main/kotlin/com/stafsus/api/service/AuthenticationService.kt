package com.stafsus.api.service

import com.stafsus.api.dto.AccessTokenDto

interface AuthenticationService {
	fun signIn(email: String, password: String): AccessTokenDto
	fun refresh(token: String): AccessTokenDto
}