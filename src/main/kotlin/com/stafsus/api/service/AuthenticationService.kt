package com.stafsus.api.service

import com.stafsus.api.dto.AccessTokenDto
import com.stafsus.api.dto.EditUserDto
import com.stafsus.api.entity.UserPrincipal
import org.springframework.transaction.annotation.Transactional

interface AuthenticationService {
	fun signIn(email: String, password: String): AccessTokenDto
	fun refresh(token: String): AccessTokenDto
//	fun editUser(editUserDto: EditUserDto): UserPrincipal
	fun editUser(editUserDto: EditUserDto, user: UserPrincipal): UserPrincipal
}