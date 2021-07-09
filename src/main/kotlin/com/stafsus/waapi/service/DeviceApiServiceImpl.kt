package com.stafsus.waapi.service

import com.stafsus.waapi.constant.MessageKey
import com.stafsus.waapi.entity.WaDevice
import com.stafsus.waapi.exception.ValidationException
import com.stafsus.waapi.feign.WaDeviceClient
import com.stafsus.waapi.repository.WaDeviceRepository
import com.stafsus.waapi.service.dto.ResponseDto
import org.springframework.stereotype.Service
import java.net.URI

@Service
class DeviceApiServiceImpl(
	private val waDeviceRepository: WaDeviceRepository,
	private val waDeviceClient: WaDeviceClient
) : DeviceApiService {
	override fun getContacts(deviceId: String): ResponseDto {
		val device = getDevice(deviceId)
		return waDeviceClient.getContacts(URI.create("http://wa-${device.deviceId}"))
	}

	override fun getChat(deviceId: String): ResponseDto {
		val device = getDevice(deviceId)
		return waDeviceClient.getChat(URI.create("http://wa-${device.deviceId}"))
	}

	override fun logout(deviceId: String): ResponseDto {
		val device = getDevice(deviceId)
		return waDeviceClient.logout(URI.create("http://wa-${device.deviceId}"))
	}

	private fun getDevice(deviceId: String): WaDevice {
		return waDeviceRepository.findByDeviceId(deviceId)
			.orElseThrow { ValidationException(MessageKey.INVALID_DEVICE_ID) }
	}
}