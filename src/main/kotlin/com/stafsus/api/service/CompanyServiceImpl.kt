package com.stafsus.api.service

import com.stafsus.api.entity.Company
import com.stafsus.api.entity.UserPrincipal
import com.stafsus.api.repository.CompanyRepository
import org.springframework.stereotype.Service

@Service
class CompanyServiceImpl(
	private val companyRepository: CompanyRepository
) : CompanyService {
	override fun getCompaniesByUser(userPrincipal: UserPrincipal): List<Company> {
		return companyRepository.findDistinctCompany(userPrincipal.id!!)
	}
}