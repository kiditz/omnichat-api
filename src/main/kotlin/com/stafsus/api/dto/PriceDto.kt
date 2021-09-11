package com.stafsus.api.dto

import com.stafsus.api.entity.Price
import com.stafsus.api.entity.Product
import java.math.BigDecimal
import javax.validation.constraints.Digits
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

data class PriceDto(
	@field:NotBlank
	var name: String? = null,
	@field:NotNull
	@field:Digits(integer = 9, fraction = 2)
	var price: BigDecimal? = null,
	@field:NotNull
	@field:Positive
	var accessTime: Long? = null,
	@field:NotNull
	@field:Positive
	var agentAmount: Int? = null,
	@field:NotNull
	@field:Positive
	var channelAmount: Int? = null,
	@field:NotNull
	@field:Positive
	var monthlyActiveVisitor: Int? = null,
	var productId: Long? = null,

	var descriptions: List<PriceDescriptionDto>? = null,
) {
	fun toEntity(product: Product?): Price {
		return Price(
			name = name!!,
			price = price!!,
			accessTime = accessTime!!,
			product = product,
			agentAmount = agentAmount,
			channelAmount = channelAmount,
			monthlyActiveVisitor = monthlyActiveVisitor
		)
	}
}
