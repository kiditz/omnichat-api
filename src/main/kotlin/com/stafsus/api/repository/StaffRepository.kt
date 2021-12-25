package com.stafsus.api.repository

import com.stafsus.api.entity.Staff
import com.stafsus.api.projection.StaffView
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface StaffRepository : JpaRepository<Staff, Long> {
	fun getByCompanyId(companyId: Long, pageable: Pageable): Page<StaffView>
}