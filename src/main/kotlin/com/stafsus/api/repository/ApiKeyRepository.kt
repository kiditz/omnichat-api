package com.stafsus.api.repository

import com.stafsus.api.entity.ApiKey
import org.springframework.data.repository.CrudRepository

interface ApiKeyRepository : CrudRepository<ApiKey, Long> {
}