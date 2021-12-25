package com.stafsus.api.repository

import com.stafsus.api.entity.Company
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CompanyRepository : JpaRepository<Company, Long> {
	@Query("SELECT DISTINCT u.company FROM UserCompany u WHERE u.id.userPrincipalId = :userPrincipalId ")
	fun findDistinctCompany(@Param("userPrincipalId") userPrincipalId: Long): List<Company>
}