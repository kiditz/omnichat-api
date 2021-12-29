package com.stafsus.api.controller

import com.stafsus.api.constant.UrlPath
import com.stafsus.api.dto.PriceDto
import com.stafsus.api.dto.ResponseDto
import com.stafsus.api.service.PriceService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(UrlPath.API_PRICE)
@Tag(name = "Price", description = "Price API")
@Validated
class PriceController(
	private val priceService: PriceService
) {
	@PostMapping
	@PreAuthorize("hasAuthority('SUPER_ADMIN')")
	@Operation(summary = "Add price", security = [SecurityRequirement(name = "bearer-key")])
	fun addPrice(
		@RequestBody priceDto: PriceDto
	): ResponseDto {
		val price = priceService.save(priceDto)
		return ResponseDto(payload = price)
	}


	@GetMapping("{productId}")
	@Operation(summary = "Get price with for product", security = [SecurityRequirement(name = "bearer-key")])
	fun findPriceByProduct(
		@PathVariable productId: Long,
		@RequestParam page: Int,
		@RequestParam size: Int,
	): ResponseDto {
		val pricePage = priceService.findAdditionalPrice(productId, page, size)
		return ResponseDto.fromPage(pricePage)
	}


	@GetMapping
	@Operation(summary = "Get price list", security = [SecurityRequirement(name = "bearer-key")])
	fun findAllPrice(
		@RequestParam page: Int,
		@RequestParam size: Int,
	): ResponseDto {
		val pricePage = priceService.findAll(page, size)
		return ResponseDto.fromPage(pricePage)
	}

}