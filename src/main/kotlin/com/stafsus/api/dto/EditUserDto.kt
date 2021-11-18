package com.stafsus.api.dto

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class EditUserDto(
	@field:Email
	@field:NotBlank
	val email: String? = null,
	@field:NotBlank
	val name: String? = null,
	val oldPassword: String? = null,
	val newPassword: String? = null,
)
