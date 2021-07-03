package com.stafsus.waapi.repository

import com.stafsus.waapi.entity.DeviceInfo
import com.stafsus.waapi.entity.WaDevice
import com.stafsus.waapi.service.dto.WaDeviceDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface WaDeviceRepository : JpaRepository<WaDevice, Long> {
	fun findByDeviceIdAndUserEmail(deviceId: String, email: String): Optional<WaDevice>
	fun findByDeviceId(deviceId: String): Optional<WaDevice>
	fun findByDeviceIdAndDeviceInfoNot(deviceId: String, deviceInfo: DeviceInfo): Optional<WaDevice>
	fun findByUserEmail(email: String, pageable: Pageable): Page<WaDeviceDto>
}