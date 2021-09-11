package com.stafsus.api.dto

import javax.validation.constraints.NotBlank

data class RefreshTokenDto(
	@field:NotBlank
	val refresh: String? = null,
)
