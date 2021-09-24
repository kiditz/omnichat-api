package com.stafsus.api.repository

import com.stafsus.api.entity.UserAuthority
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserAuthorityRepository : JpaRepository<UserAuthority, Long> {
	fun findByAuthority(authority: String): Optional<UserAuthority>
}