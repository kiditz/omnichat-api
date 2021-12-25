package com.stafsus.api.repository

import com.stafsus.api.entity.Staff
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface StaffRepository : JpaRepository<Staff, Long> {
	fun findByEmailAndCompanyId(email: String, companyId: Long): Optional<Staff>
	fun getByCompanyId(companyId: Long, pageable: Pageable): Page<Staff>
}