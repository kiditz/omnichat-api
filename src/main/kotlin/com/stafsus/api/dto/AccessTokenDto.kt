package com.stafsus.api.dto

data class AccessTokenDto(
	var accessToken: String? = null,
	var refreshToken: String? = null,
	var expiryDate: Long? = null,
)