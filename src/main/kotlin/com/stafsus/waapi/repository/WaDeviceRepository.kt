package com.stafsus.waapi.repository

import com.stafsus.waapi.entity.DeviceInfo
import com.stafsus.waapi.entity.WaDevice
import org.springframework.data.repository.CrudRepository
import java.util.*

interface WaDeviceRepository : CrudRepository<WaDevice, Long> {
    fun findByDeviceIdAndUserEmail(deviceId: String, email: String): Optional<WaDevice>
    fun findByDeviceId(deviceId: String): Optional<WaDevice>
    fun findByDeviceIdAndDeviceInfoNot(deviceId: String, deviceInfo: DeviceInfo): Optional<WaDevice>
}