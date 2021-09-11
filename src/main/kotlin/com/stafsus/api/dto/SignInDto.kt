package com.stafsus.api.dto

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class SignInDto(
	@field:Email
	@field:NotBlank
	@field:Size(max = 100)
	val email: String? = null,
	@field:NotBlank
	@field:Size(min = 8)
	val password: String? = null,
)
