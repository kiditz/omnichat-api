package com.stafsus.api.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class VerifyStaffDto(
	@field:NotNull
	@field:NotBlank
	val invitationCode: String? = null,
)

