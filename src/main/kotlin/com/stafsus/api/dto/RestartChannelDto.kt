package com.stafsus.api.dto

import javax.validation.constraints.NotBlank

data class RestartChannelDto(
	@field:NotBlank
	var deviceId: String? = null,
)
