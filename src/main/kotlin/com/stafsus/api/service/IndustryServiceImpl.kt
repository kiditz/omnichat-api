package com.stafsus.api.service

import com.stafsus.api.projection.IndustryProjection
import com.stafsus.api.repository.IndustryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class IndustryServiceImpl(
	private val industryRepository: IndustryRepository
) : IndustryService {
	@Transactional(readOnly = true)
	override fun getAll(): List<IndustryProjection> {
		return industryRepository.findByOrderByName()
	}
}