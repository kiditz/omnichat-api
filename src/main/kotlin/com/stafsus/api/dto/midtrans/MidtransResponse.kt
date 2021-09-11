package com.stafsus.api.dto.midtrans

import com.fasterxml.jackson.annotation.JsonProperty

data class MidtransResponse(

	@field:JsonProperty("redirect_url")
	val redirectUrl: String? = null,

	@field:JsonProperty("error_messages")
	val errorMessages: List<String?>? = null,

	@field:JsonProperty("token")
	val token: String? = null
) {
	fun toDto(): MidtransDto {
		return MidtransDto(redirectUrl, errorMessages, token)
	}
}
