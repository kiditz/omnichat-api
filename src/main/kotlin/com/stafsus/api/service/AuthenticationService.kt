package com.stafsus.api.service

import com.stafsus.api.dto.AccessTokenDto
import com.stafsus.api.entity.UserAuthority

interface AuthenticationService {
	fun signIn(email: String, password: String): AccessTokenDto
	fun refresh(token: String): AccessTokenDto
	fun getAuthorities(): List<UserAuthority>
}