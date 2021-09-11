package com.stafsus.api.dto.midtrans

import com.fasterxml.jackson.annotation.JsonInclude

data class MidtransDto(
	@JsonInclude(JsonInclude.Include.NON_NULL)
	val redirectUrl: String? = null,
	@JsonInclude(JsonInclude.Include.NON_NULL)
	val errorMessages: List<String?>? = null,
	@JsonInclude(JsonInclude.Include.NON_NULL)
	val token: String? = null
)
