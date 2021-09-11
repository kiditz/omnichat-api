package com.stafsus.api.controller

import com.stafsus.api.constant.UrlPath
import com.stafsus.api.dto.ResponseDto
import com.stafsus.api.dto.TransactionDto
import com.stafsus.api.dto.UserDetailDto
import com.stafsus.api.service.TransactionService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping(UrlPath.TRANSACTION)
@Tag(name = "Transaction", description = "Tag API")
@Validated
class TransactionController(
	private val transactionService: TransactionService
) {
	@PostMapping
	@PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
	@Operation(summary = "Buy Package", security = [SecurityRequirement(name = "bearer-key")])
	fun buyPackage(@Valid @RequestBody request: TransactionDto, authentication: Authentication): ResponseDto {
		val user = (authentication.principal as UserDetailDto).user
		val transaction = transactionService.buyPackage(user, request.priceId)
		return ResponseDto(payload = transaction)
	}
}