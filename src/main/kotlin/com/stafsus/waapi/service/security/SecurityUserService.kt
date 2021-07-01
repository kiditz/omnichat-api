package com.stafsus.waapi.service.security

import com.stafsus.waapi.service.dto.UserPrincipal
import org.springframework.security.core.userdetails.UserDetailsService
import java.util.*

interface SecurityUserService : UserDetailsService {

    fun loadUserId(id: Long): Optional<UserPrincipal>
    fun isBlocked(accessToken: String): Boolean
}