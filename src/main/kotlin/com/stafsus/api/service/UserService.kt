package com.stafsus.api.service

import com.stafsus.api.entity.UserPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService

interface UserService : UserDetailsService {
	fun save(userPrincipal: UserPrincipal): UserPrincipal
	fun loadUserById(id: Long): UserDetails
}