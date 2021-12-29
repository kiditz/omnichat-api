package com.stafsus.api.controller

import com.stafsus.api.constant.UrlPath
import com.stafsus.api.dto.ResponseDto
import com.stafsus.api.service.IndustryService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(UrlPath.API_INDUSTRY)
@Tag(name = "Industry", description = "Industry API")
@Validated
class IndustryController(
	private val industryService: IndustryService,
) {
	@GetMapping
	@Operation(summary = "Get all industry")
	fun getIndustries(): ResponseDto {
		val industries = industryService.getAll();
		return ResponseDto(payload = industries)
	}
}