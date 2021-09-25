package com.stafsus.api.repository

import com.stafsus.api.entity.Department
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface DepartmentRepository : JpaRepository<Department, Long> {
	fun findByNameContainingIgnoreCaseAndCompanyId(name: String?, userId: Long, pageable: Pageable): Page<Department>
}