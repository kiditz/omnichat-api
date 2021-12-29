package com.stafsus.api.controller

import com.stafsus.api.constant.UrlPath
import com.stafsus.api.dto.ProductDto
import com.stafsus.api.dto.ResponseDto
import com.stafsus.api.entity.ProductType
import com.stafsus.api.service.ProductService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping(UrlPath.API_PRODUCT)
@Tag(name = "Product", description = "Product API")
@Validated
class ProductController(
	private val productService: ProductService
) {
	@PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
	@PreAuthorize("hasAuthority('SUPER_ADMIN')")
	@Operation(summary = "Add product", security = [SecurityRequirement(name = "bearer-key")])
	fun addProduct(
		@RequestParam name: String,
		@RequestParam(required = false) descriptionEn: String? = null,
		@RequestParam(required = false) descriptionId: String? = null,
		@RequestParam priority: Long,
		@RequestParam type: ProductType,
		@RequestParam file: MultipartFile,
	): ResponseDto {
		val productDto = ProductDto(name, descriptionEn, descriptionId, priority, type, file)
		val product = productService.save(productDto)
		return ResponseDto(payload = product)
	}

	@GetMapping
	@Operation(summary = "Get product for channel list", security = [SecurityRequirement(name = "bearer-key")])
	fun findProductChannel(
		@RequestParam page: Int,
		@RequestParam size: Int,
	): ResponseDto {
		val products = productService.findProductChannels(page, size)
		return ResponseDto.fromPage(products)
	}
}