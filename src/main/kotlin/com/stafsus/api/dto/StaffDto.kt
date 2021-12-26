package com.stafsus.api.dto

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class StaffDto(
	@field:NotBlank
	val name: String? = null,
	@field:Email
	@field:NotBlank
	@field:Size(max = 100)
	val email: String? = null,
	@field:NotBlank
	@field:Size(min = 8)
	val password: String? = null,
	@field:NotBlank
	val authority: String? = null,
	val department: Long? = null,
	val channels: Set<Long> = mutableSetOf(),
)