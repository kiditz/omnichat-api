package com.stafsus.waapi.service.dto

data class TokenDto(
	var accessToken: String? = null,
	var refreshToken: String? = null,
	var expiryDate: Long? = null,
)