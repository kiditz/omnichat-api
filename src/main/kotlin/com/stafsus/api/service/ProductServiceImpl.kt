package com.stafsus.api.service

import com.stafsus.api.dto.ProductDto
import com.stafsus.api.entity.Product
import com.stafsus.api.entity.ProductType
import com.stafsus.api.repository.ProductRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductServiceImpl(
	private val productRepository: ProductRepository,
	private val fileService: FileService
) : ProductService {

	@Transactional
	override fun save(product: ProductDto): Product {
		val fileName = fileService.save(product.file!!)
		val imageUrl = fileService.getImageUrl(fileName)
		return productRepository.save(product.toEntity(imageUrl))
	}

	override fun findProductChannels(page: Int, size: Int): Page<Product> {
		return productRepository.findAllByTypeNot(
			ProductType.QUOTA,
			PageRequest.of(page, size).withSort(Sort.Direction.ASC, "priority")
		)
	}

	override fun findProductQuotas(page: Int, size: Int): Page<Product> {
		return productRepository.findAllByType(
			ProductType.QUOTA,
			PageRequest.of(page, size).withSort(Sort.by("name"))
		)
	}

}