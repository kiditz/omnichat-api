package com.stafsus.api.service

import com.stafsus.api.constant.MessageKey
import com.stafsus.api.entity.ApiKey
import com.stafsus.api.exception.ValidationException
import com.stafsus.api.repository.ApiKeyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ApiKeyServiceImpl(
	private val companyService: CompanyService,
	private val apiKeyRepository: ApiKeyRepository
) : ApiKeyService {

	@Transactional(readOnly = true)
	override fun findApiKey(): ApiKey {
		val company = companyService.getCompany()
		return apiKeyRepository.findByCompanyId(company.id!!)
			.orElseThrow { ValidationException(MessageKey.API_KEY_NOT_FOUND) }
	}
}