package com.stafsus.api.repository

import com.stafsus.api.entity.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RefreshTokenRepository : JpaRepository<RefreshToken, Long> {
	fun findByToken(token: String): Optional<RefreshToken>
	fun deleteByToken(token: String)
}