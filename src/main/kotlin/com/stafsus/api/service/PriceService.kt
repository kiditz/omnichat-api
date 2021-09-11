package com.stafsus.api.service

import com.stafsus.api.dto.PriceDto
import com.stafsus.api.entity.Price
import org.springframework.data.domain.Page
import org.springframework.transaction.annotation.Transactional

interface PriceService {
	fun save(priceDto: PriceDto): Price
	fun findAdditionalPrice(productId: Long, page: Int, size: Int): Page<Price>
	@Transactional(readOnly = true)
	fun findAll(page: Int, size: Int): Page<Price>
}