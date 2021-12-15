package com.stafsus.api.repository

import com.stafsus.api.entity.Product
import com.stafsus.api.entity.ProductType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ProductRepository : JpaRepository<Product, Long> {
	fun findAllByType(type: ProductType, pageable: Pageable): Page<Product>
	fun findByTypeIn(type: Set<ProductType>): List<Product>
	fun findAllByTypeNot(type: ProductType, pageable: Pageable): Page<Product>
}