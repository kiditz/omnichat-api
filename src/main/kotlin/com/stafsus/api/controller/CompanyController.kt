package com.stafsus.api.controller

import com.stafsus.api.constant.UrlPath
import com.stafsus.api.dto.ResponseDto
import com.stafsus.api.dto.UserDetailDto
import com.stafsus.api.service.CompanyService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.Authentication
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(UrlPath.API_COMPANY)
@Tag(name = "Company", description = "Company API")
@Validated
class CompanyController(
	private val companyService: CompanyService
) {
	@GetMapping
	@Operation(summary = "Get companies by logged in user", security = [SecurityRequirement(name = "bearer-key")])
	fun getCompanies(authentication: Authentication): ResponseDto {
		val user = (authentication.principal as UserDetailDto).user
		val companies = companyService.getCompaniesByUser(user)
		return ResponseDto(payload = companies)
	}
}