package com.stafsus.api.service

import com.stafsus.api.dto.EditUserDto
import com.stafsus.api.dto.SignUpDto
import com.stafsus.api.entity.UserPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.multipart.MultipartFile

interface UserService : UserDetailsService {
	fun signUp(signUpDto: SignUpDto): UserPrincipal
	fun loadUserById(id: Long): UserDetails
	fun editUser(editUserDto: EditUserDto, user: UserPrincipal): UserPrincipal
	fun updateImage(user: UserPrincipal, multipartFile: MultipartFile): UserPrincipal
}