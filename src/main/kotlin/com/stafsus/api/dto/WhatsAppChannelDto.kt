package com.stafsus.api.dto

data class WhatsAppChannelDto(
	var phone: String? = null,
	var browserSession: String? = null,
	var qrCode: String? = null,
	var deviceId: String? = null,
	val pushName: String? = null,
	var isOnline: Boolean? = null,
	var isActive: Boolean? = null,
	var isPending: Boolean? = null,
)
