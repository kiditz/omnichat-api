package com.stafsus.api.repository

import com.stafsus.api.entity.UserCompany
import com.stafsus.api.entity.UserCompanyId
import org.springframework.data.jpa.repository.JpaRepository

interface UserCompanyRepository : JpaRepository<UserCompany, UserCompanyId> {
	fun getByUserPrincipalIdAndCompanyId(userPrincipalId: Long, companyId: Long): List<UserCompany>
}