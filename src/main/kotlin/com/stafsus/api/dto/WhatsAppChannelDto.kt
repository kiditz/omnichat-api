package com.stafsus.api.dto

import com.stafsus.api.entity.ChannelStatus
import com.stafsus.api.entity.DeviceStatus

data class WhatsAppChannelDto(
	var phone: String? = null,
	var browserSession: String? = null,
	var qrCode: String? = null,
	var deviceId: String? = null,
	val pushName: String? = null,
	var status: ChannelStatus? = null,
	var deviceStatus: DeviceStatus? = null,
)
