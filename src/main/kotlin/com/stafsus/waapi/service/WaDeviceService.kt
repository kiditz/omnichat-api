package com.stafsus.waapi.service

import com.stafsus.waapi.entity.DeviceInfo
import com.stafsus.waapi.entity.DeviceStatus
import com.stafsus.waapi.entity.User
import com.stafsus.waapi.entity.WaDevice
import com.stafsus.waapi.service.dto.ResponseDto
import com.stafsus.waapi.service.dto.DeviceDto
import java.security.Principal
import java.util.*

interface WaDeviceService {
    fun getQrCode(deviceId: String): ResponseDto
    fun install(principal: Principal): DeviceDto
    fun install(user: User): DeviceDto
    fun uninstall(deviceId: String, email: String)
    fun restart(deviceId: String, email: String)
    fun updateDeviceInfo(deviceId: String, deviceInfo: DeviceInfo)
    fun updateDeviceStatus(deviceId: String, deviceStatus: DeviceStatus)
    fun validateDevice(deviceId: String): WaDevice?
    fun findByDeviceId(deviceId: String): Optional<WaDevice>
}