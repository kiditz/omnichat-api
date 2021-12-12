package com.stafsus.api.repository

import com.stafsus.api.entity.ApiKey
import org.springframework.data.repository.CrudRepository
import java.util.*

interface ApiKeyRepository : CrudRepository<ApiKey, Long> {
	fun findByCompanyId(companyId: Long): Optional<ApiKey>
}