package com.stafsus.waapi.service.security

import com.stafsus.waapi.service.dto.SignInDto
import com.stafsus.waapi.service.dto.SignUpDto
import com.stafsus.waapi.service.dto.TokenDto
import com.stafsus.waapi.service.dto.UserDto

interface AuthenticationService {
	fun signIn(signInDto: SignInDto): TokenDto
	fun signUp(signUpDto: SignUpDto): UserDto
	fun refreshToken(token: String): TokenDto
	fun signOut(refreshToken: String, accessToken: String)
	fun findUser(email: String): UserDto
}