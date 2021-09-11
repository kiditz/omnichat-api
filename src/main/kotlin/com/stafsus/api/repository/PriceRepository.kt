package com.stafsus.api.repository

import com.stafsus.api.entity.Price
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PriceRepository : JpaRepository<Price, Long> {
	fun findByProductId(productId: Long, pageable: Pageable): Page<Price>
	fun findByNameNot(name: String, pageable: Pageable): Page<Price>
	fun findByName(name: String): Optional<Price>
}