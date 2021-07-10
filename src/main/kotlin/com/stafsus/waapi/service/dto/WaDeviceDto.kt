package com.stafsus.waapi.service.dto

import com.stafsus.waapi.entity.DeviceInfo
import com.stafsus.waapi.entity.DeviceStatus
import java.time.LocalDateTime

data class WaDeviceDto(
	val id: Long? = null,
	var deviceId: String,
	var phone: String? = null,
	var deviceStatus: DeviceStatus? = null,
	var deviceInfo: DeviceInfo? = null,
	var startAt: LocalDateTime? = null,
	var endAt: LocalDateTime? = null,
	var isTrial: Boolean? = null,
	var session: String? = null,
)
