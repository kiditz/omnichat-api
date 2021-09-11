package com.stafsus.api.dto

import com.stafsus.api.entity.Product
import com.stafsus.api.entity.ProductType
import org.springframework.web.multipart.MultipartFile
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive
import javax.validation.constraints.Size

data class ProductDto(
	@field:NotBlank
	@field:Size(max = 60)
	val name: String? = null,
	val descriptionEn: String? = null,
	val descriptionId: String? = null,
	@field:Positive
	val priority: Long? = null,
	@field:NotNull
	val type: ProductType? = null,
	@field:NotNull
	val file: MultipartFile? = null,
) {
	fun toEntity(imageUrl: String): Product {
		return Product(
			name = name!!,
			descriptionEn = descriptionEn,
			descriptionId = descriptionId,
			imageUrl = imageUrl,
			priority = priority!!,
			type = type,
		)
	}
}
