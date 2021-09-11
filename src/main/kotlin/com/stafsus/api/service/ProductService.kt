package com.stafsus.api.service

import com.stafsus.api.dto.ProductDto
import com.stafsus.api.entity.Product
import org.springframework.data.domain.Page

interface ProductService {
	fun save(product: ProductDto): Product
	fun findProductChannels(page: Int, size: Int): Page<Product>
	fun findProductQuotas(page: Int, size: Int): Page<Product>
}