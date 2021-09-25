package com.stafsus.api.service

import com.stafsus.api.dto.SignUpDto
import com.stafsus.api.dto.StaffSignUpDto
import com.stafsus.api.entity.UserPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService

interface UserService : UserDetailsService {
	fun signUp(signUpDto: SignUpDto): UserPrincipal
	fun invitationSignUp(signUpDto: StaffSignUpDto): UserPrincipal
	fun loadUserById(id: Long): UserDetails
}