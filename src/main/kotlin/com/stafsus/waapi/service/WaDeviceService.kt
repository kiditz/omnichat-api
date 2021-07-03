package com.stafsus.waapi.service

import com.stafsus.waapi.entity.*
import com.stafsus.waapi.service.dto.ResponseDto
import com.stafsus.waapi.service.dto.DeviceDto
import com.stafsus.waapi.service.dto.WaDeviceDto
import org.springframework.data.domain.Page
import org.springframework.transaction.annotation.Transactional
import java.security.Principal
import java.util.*

interface WaDeviceService {
	fun getQrCode(deviceId: String): QrCode
	fun install(principal: Principal): DeviceDto
	fun startTrial(principal: Principal): DeviceDto
	fun install(user: User): DeviceDto
	fun uninstall(deviceId: String, email: String)
	fun restart(deviceId: String, email: String)
	fun updateDeviceInfo(deviceId: String, deviceInfo: DeviceInfo)
	fun validateDevice(deviceId: String): WaDevice?
	fun findByDeviceId(deviceId: String): Optional<WaDevice>
	fun updateDeviceStatus(deviceId: String, deviceStatus: DeviceStatus)
	fun findDevices(email: String, page: Int, size: Int): Page<WaDeviceDto>
	fun updateDeviceStatus(deviceId: String, phone: String, deviceStatus: DeviceStatus)
}