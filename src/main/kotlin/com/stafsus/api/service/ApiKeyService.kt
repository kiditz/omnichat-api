package com.stafsus.api.service

import com.stafsus.api.entity.ApiKey

interface ApiKeyService {
	fun findApiKey(): ApiKey
}