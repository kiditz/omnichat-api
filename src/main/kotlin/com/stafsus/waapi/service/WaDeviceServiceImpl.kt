package com.stafsus.waapi.service

import com.stafsus.waapi.config.RabbitConfig
import com.stafsus.waapi.constant.MessageKey
import com.stafsus.waapi.entity.*
import com.stafsus.waapi.exception.ValidationException
import com.stafsus.waapi.repository.QrCodeRepository
import com.stafsus.waapi.repository.UserRepository
import com.stafsus.waapi.repository.WaDeviceRepository
import com.stafsus.waapi.service.dto.DeviceDto
import com.stafsus.waapi.service.dto.WaDeviceDto
import com.stafsus.waapi.utils.Random
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import java.security.Principal
import java.time.LocalDateTime
import java.util.*


@Service
class WaDeviceServiceImpl(
	private val rabbitTemplate: RabbitTemplate,
	private val deviceRepository: WaDeviceRepository,
	private val userRepository: UserRepository,
	private val qrCodeRepository: QrCodeRepository,
	@Value("\${app.device.period}") val period: Long,

	) : WaDeviceService {
	private val log = LoggerFactory.getLogger(javaClass)

	@Transactional(readOnly = true)
	override fun getQrCode(deviceId: String): QrCode {
		val device = validateDevice(deviceId)
		if (device.deviceStatus == DeviceStatus.PHONE_ONLINE) {
			throw ValidationException(MessageKey.PHONE_ALREADY_ONLINE)
		}
		return qrCodeRepository.findById(deviceId).orElseThrow { ValidationException(MessageKey.QR_NOT_READY) }
	}

	override fun validateDevice(deviceId: String): WaDevice {
		val device =
			deviceRepository.findByDeviceId(deviceId).orElseThrow { ValidationException(MessageKey.INVALID_DEVICE_ID) }
		if (device.deviceInfo != DeviceInfo.ACTIVE) {
			throw ValidationException(MessageKey.INACTIVE_DEVICE)
		}
		return device
	}

	override fun findByDeviceId(deviceId: String): Optional<WaDevice> {
		return deviceRepository.findByDeviceId(deviceId)
	}


	@Transactional
	override fun install(principal: Principal): DeviceDto {
		val user =
			userRepository.findByEmail(principal.name).orElseThrow { ValidationException(MessageKey.USER_NOT_FOUND) }
		return install(user)
	}

	@Transactional
	override fun install(user: User): DeviceDto {
		val startAt = LocalDateTime.now()
		val endAt = LocalDateTime.now().plusDays(period)
		val device = WaDevice(
			deviceId = Random.stringLowerOnly(10),
			startAt = startAt,
			endAt = endAt,
			deviceStatus = DeviceStatus.PHONE_OFFLINE,
			isTrial = true
		)
		device.user = user
		deviceRepository.save(device)
		log.info("Install Device :${device.deviceId}")
		val result = mutableMapOf(
			"deviceId" to device.deviceId,
			"deviceInfo" to DeviceInfo.ON_INSTALL.name
		)
		rabbitTemplate.convertAndSend(RabbitConfig.INSTALL_EX, RabbitConfig.INSTALL_RK, result)
		return DeviceDto(deviceId = device.deviceId, accessedBy = user.email)
	}

	@Transactional
	override fun startTrial(principal: Principal): DeviceDto {
		val user = userRepository.findByEmail(principal.name)
			.orElseThrow { ValidationException(MessageKey.USER_NOT_FOUND) }
		if (isTrialExpired(user)) {
			throw ValidationException(MessageKey.TRIAL_EXPIRED)
		}
		user.startTrialAt = LocalDateTime.now()
		user.endTrialAt = LocalDateTime.now().plusDays(period)
		userRepository.save(user)
		return install(user)
	}

	private fun isTrialExpired(user: User) =
		user.endTrialAt != null && LocalDateTime.now().isAfter(user.endTrialAt)

	@Transactional(readOnly = true)
	override fun uninstall(deviceId: String, email: String) {
		log.info("Uninstall Device :${deviceId}:${email}")
		deviceRepository.findByDeviceIdAndUserEmail(deviceId, email)
			.orElseThrow { ValidationException(MessageKey.INVALID_DEVICE_ID) }
		val result = mutableMapOf(
			"deviceId" to deviceId,
			"deviceInfo" to DeviceInfo.ON_UNINSTALLED.name
		)
		rabbitTemplate.convertAndSend(RabbitConfig.UNINSTALL_EX, RabbitConfig.UNINSTALL_RK, result)
	}

	@Transactional(readOnly = true)
	override fun restart(deviceId: String, email: String) {
		log.info("Restart Device :${deviceId}:${email}")
		deviceRepository.findByDeviceIdAndUserEmail(deviceId, email)
			.orElseThrow { ValidationException(MessageKey.INVALID_DEVICE_ID) }
		val result = mutableMapOf(
			"deviceId" to deviceId,
			"deviceInfo" to DeviceInfo.ON_RESTART.name
		)
		rabbitTemplate.convertAndSend(RabbitConfig.RESTART_EX, RabbitConfig.RESTART_RK, result)
	}

	@Transactional
	override fun updateDeviceInfo(deviceId: String, deviceInfo: DeviceInfo) {
		val device =
			deviceRepository.findByDeviceId(deviceId)
				.orElseThrow { ValidationException(MessageKey.INVALID_DEVICE_ID) }
		device.deviceInfo = deviceInfo
		deviceRepository.save(device)
	}

	@Transactional
	override fun authenticatedSession(deviceId: String, session: String) {
		val device =
			deviceRepository.findByDeviceId(deviceId)
				.orElseThrow { ValidationException(MessageKey.INVALID_DEVICE_ID) }
		device.session = session
		deviceRepository.save(device)
	}

	@Transactional
	override fun updateDeviceStatus(deviceId: String, deviceStatus: DeviceStatus) {
		return updateDeviceStatus(deviceId, "", deviceStatus)
	}

	@Transactional
	override fun updateDeviceStatus(deviceId: String, phone: String, deviceStatus: DeviceStatus) {
		val device =
			deviceRepository.findByDeviceId(deviceId)
				.orElseThrow { ValidationException(MessageKey.INVALID_DEVICE_ID) }
		device.deviceStatus = deviceStatus
		if (deviceStatus == DeviceStatus.PHONE_OFFLINE) {
			device.session = null
		}
		if (StringUtils.hasText(phone))
			device.phone = phone
		deviceRepository.save(device)
	}

	@Transactional(readOnly = true)
	override fun findDevices(email: String, page: Int, size: Int): Page<WaDeviceDto> {
		return deviceRepository
			.findByUserEmail(email, PageRequest.of(page, size, Sort.by("id").descending()))
	}
}