package com.stafsus.api.repository

import com.stafsus.api.entity.Staff
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface StaffRepository : JpaRepository<Staff, Long> {
	fun findByEmailAndCompanyId(email: String, companyId: Long): Optional<Staff>
	fun findByInvitationCode(invitationCode: String): Optional<Staff>
	fun existsByInvitationCode(invitationCode: String): Boolean
}