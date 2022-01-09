package com.stafsus.api.repository

import com.stafsus.api.entity.Staff
import com.stafsus.api.entity.Status
import com.stafsus.api.entity.UserPrincipal
import com.stafsus.api.projection.StaffView
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface StaffRepository : JpaRepository<Staff, Long> {
	fun getByCompanyIdAndStatus(companyId: Long, status: Status, pageable: Pageable): Page<StaffView>
	fun findByUserEmail(email: String): Optional<Staff>
}