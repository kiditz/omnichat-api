package com.stafsus.api.repository

import com.stafsus.api.entity.Company
import org.springframework.data.jpa.repository.JpaRepository

interface CompanyRepository : JpaRepository<Company, Long> {
//	fun findByUserId(userId: Long): Optional<Company>
}