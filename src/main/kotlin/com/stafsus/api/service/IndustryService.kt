package com.stafsus.api.service

import com.stafsus.api.projection.IndustryProjection

interface IndustryService {
	fun getAll(): List<IndustryProjection>
}