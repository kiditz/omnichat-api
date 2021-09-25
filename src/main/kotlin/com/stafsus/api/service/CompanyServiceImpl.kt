package com.stafsus.api.service

import com.stafsus.api.config.ThreadLocalStorage
import com.stafsus.api.constant.MessageKey
import com.stafsus.api.entity.Company
import com.stafsus.api.entity.UserPrincipal
import com.stafsus.api.exception.ValidationException
import com.stafsus.api.repository.CompanyRepository
import org.springframework.stereotype.Service

@Service
class CompanyServiceImpl(
	private val companyRepository: CompanyRepository
) : CompanyService {
	override fun getCompaniesByUser(userPrincipal: UserPrincipal): List<Company> {
		return companyRepository.findDistinctCompany(userPrincipal.id!!)
	}

	override fun getCompany(): Company {
		val companyId = ThreadLocalStorage.getTenantId()
			?: throw ValidationException(MessageKey.COMPANY_REQUIRED)
		return companyRepository.findById(companyId)
			.orElseThrow { ValidationException(MessageKey.COMPANY_NOT_FOUND) }
	}
}