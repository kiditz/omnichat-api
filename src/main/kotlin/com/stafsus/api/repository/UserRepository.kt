package com.stafsus.api.repository

import com.stafsus.api.entity.UserPrincipal
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<UserPrincipal, Long> {
    fun findByEmail(email: String): Optional<UserPrincipal>
    fun existsByEmail(email: String): Boolean
}