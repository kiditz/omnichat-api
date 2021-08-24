package com.stafsus.waapi.service

import com.stafsus.waapi.constant.MessageKey
import com.stafsus.waapi.entity.DeviceStatus
import com.stafsus.waapi.entity.WaDevice
import com.stafsus.waapi.exception.ValidationException
import com.stafsus.waapi.feign.WaDeviceClient
import com.stafsus.waapi.repository.WaDeviceRepository
import com.stafsus.waapi.service.dto.ResponseDto
import org.springframework.stereotype.Service
import java.net.URI

@Service
class WhatsMessageServiceImpl(
	private val waDeviceRepository: WaDeviceRepository,
	private val waDeviceClient: WaDeviceClient
) : WhatsMessageService {
	override fun getContacts(deviceId: String): ResponseDto {
		val device = getDevice(deviceId)
		return waDeviceClient.getContacts(URI.create("http://wa-${device.deviceId}"))
	}

	override fun getChat(deviceId: String): ResponseDto {
		val device = getDevice(deviceId)
		return waDeviceClient.getChat(URI.create("http://wa-${device.deviceId}"))
	}

	override fun getInfo(deviceId: String): ResponseDto {
		val device = getDevice(deviceId)
		return waDeviceClient.getInfo(URI.create("http://wa-${device.deviceId}"))
	}

	override fun downloadMedia(deviceId: String, chatId:String, messageId:String): ResponseDto {
		val device = getDevice(deviceId)
		return waDeviceClient.downloadMedia(URI.create("http://wa-${device.deviceId}"), chatId, messageId)
	}

	override fun getChatDetail(deviceId: String, chatId: String): ResponseDto {
		val device = getDevice(deviceId)
		return waDeviceClient.getChatDetail(URI.create("http://wa-${device.deviceId}"), chatId)
	}


	private fun getDevice(deviceId: String): WaDevice {
		val device = waDeviceRepository.findByDeviceId(deviceId)
			.orElseThrow { ValidationException(MessageKey.INVALID_DEVICE_ID) }
		if (device.deviceStatus == DeviceStatus.PHONE_OFFLINE) {
			throw ValidationException(MessageKey.DEVICE_OFFLINE)
		}
		return device
	}
}