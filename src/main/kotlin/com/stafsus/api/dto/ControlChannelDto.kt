package com.stafsus.api.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class ControlChannelDto(
	@field:NotBlank
	var deviceId: String? = null,
	@field:NotNull
	var active: Boolean,
)
