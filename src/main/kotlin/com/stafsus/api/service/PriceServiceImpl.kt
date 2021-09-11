package com.stafsus.api.service

import com.stafsus.api.constant.MessageKey
import com.stafsus.api.dto.PriceDto
import com.stafsus.api.entity.Price
import com.stafsus.api.entity.PriceDescription
import com.stafsus.api.entity.PriceType
import com.stafsus.api.execption.ValidationException
import com.stafsus.api.repository.PriceRepository
import com.stafsus.api.repository.ProductRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PriceServiceImpl(
	private val priceRepository: PriceRepository,
	private val productRepository: ProductRepository
) : PriceService {
	@Transactional
	override fun save(priceDto: PriceDto): Price {
		val price = if (priceDto.productId != null) {
			val product = productRepository.findById(priceDto.productId!!)
				.orElseThrow { ValidationException(MessageKey.PRODUCT_NOT_FOUND) }
			val newPrice = priceDto.toEntity(product)
			newPrice.priceType = PriceType.ADDITION
			newPrice
		} else {
			val newPrice = priceDto.toEntity(null)
			newPrice.priceType = PriceType.SUBSCRIBE
			newPrice
		}
		price.descriptions = priceDto.descriptions?.map {
			PriceDescription(
				field = it.field,
				fieldType = it.fieldType,
				fieldValue = it.fieldValue,
				price = price
			)
		}
		return priceRepository.save(price)
	}

	@Transactional(readOnly = true)
	override fun findAdditionalPrice(productId: Long, page: Int, size: Int): Page<Price> {
		return priceRepository.findByProductId(productId, PageRequest.of(page, size).withSort(Sort.Direction.ASC, "id"))
	}


	@Transactional(readOnly = true)
	override fun findAll(page: Int, size: Int): Page<Price> {
		return priceRepository.findByNameNot(
			Price.TRIAL,
			PageRequest.of(page, size).withSort(Sort.by("id").ascending())
		)
	}

}