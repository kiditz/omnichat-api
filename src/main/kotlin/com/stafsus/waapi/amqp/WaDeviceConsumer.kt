package com.stafsus.waapi.amqp

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.stafsus.waapi.config.RabbitConfig
import com.stafsus.waapi.entity.DeviceInfo
import com.stafsus.waapi.entity.DeviceStatus
import com.stafsus.waapi.entity.QrCode
import com.stafsus.waapi.exception.ValidationException
import com.stafsus.waapi.repository.QrCodeRepository
import com.stafsus.waapi.service.TranslateService
import com.stafsus.waapi.service.WaDeviceService
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime


@Component
class WaDeviceConsumer(
	private val waDeviceService: WaDeviceService,
	private val objectMapper: ObjectMapper,
	private val messagingTemplate: SimpMessagingTemplate,
	private val translateService: TranslateService,
	private val qrCodeRepository: QrCodeRepository
) {
	private val log = LoggerFactory.getLogger(javaClass)


	@RabbitListener(queues = [RabbitConfig.LOGS_Q])
	@Throws(ValidationException::class)
	fun deviceInfoState(data: Map<String, Any>) {
		log.info("Data Received : $data")
		val deviceId = data["deviceId"] as String
		val deviceInfo = data["deviceInfo"] as String
		val device = waDeviceService.findByDeviceId(deviceId = deviceId).orElse(null)
		val currentInfo = DeviceInfo.valueOf(deviceInfo)
		val message = mapOf<String, Any>(
			"message" to translateService.toLocale(currentInfo.display),
			"info" to currentInfo.name
		)
		messagingTemplate.convertAndSendToUser(
			device.user!!.email,
			"/queue/device",
			message
		)
		waDeviceService.updateDeviceInfo(deviceId, currentInfo)
	}

	@Throws(ValidationException::class)
	@RabbitListener(queues = [RabbitConfig.READY_Q])
	fun statusReady(message: Message) {
		log.info("statusReady Data Received : ${String(message.body, StandardCharsets.UTF_8)}")
		val data = objectMapper.readValue<Map<String, Any>>(message.body)
		val deviceId = data["deviceId"] as String
		val deviceStatus = data["status"] as String
		val phone = data["phone"] as String
		waDeviceService.updateDeviceStatus(deviceId, phone, DeviceStatus.valueOf(deviceStatus))
		sendStatus(deviceId, data)
	}

	@Throws(ValidationException::class)
	@RabbitListener(queues = [RabbitConfig.AUTHENTICATION_FAILURE_Q])
	fun statusOffline(message: Message) {
		log.info("statusOffline Data Received :  ${String(message.body, StandardCharsets.UTF_8)}")
		val data = objectMapper.readValue<Map<String, Any>>(message.body)
		val deviceId = data["deviceId"] as String
		val deviceStatus = data["status"] as String
		waDeviceService.updateDeviceStatus(deviceId, DeviceStatus.valueOf(deviceStatus))
		sendStatus(deviceId, data)
	}

	private fun sendStatus(deviceId: String, data: Map<String, Any>) {
		val device = waDeviceService.findByDeviceId(deviceId = deviceId).orElse(null)
		if (device != null) {
			messagingTemplate.convertAndSendToUser(device.user!!.email, "/queue/status", data)
		}
	}

	@RabbitListener(queues = [RabbitConfig.ERROR_Q])
	@Throws(ValidationException::class)
	fun errorReceived(data: Map<String, Any>) {
		log.info("Errors : $data")
	}

	@RabbitListener(queues = [RabbitConfig.QR_Q])
	@Throws(ValidationException::class)
	@Transactional
	fun qrReceived(data: Map<String, Any>) {
		val deviceId = data["deviceId"] as String
		val device = waDeviceService.findByDeviceId(deviceId = deviceId).orElse(null)
		if (device != null) {
			val qr = data["qrCode"] as String
			val qrCode = qrCodeRepository.findById(deviceId).orElse(QrCode(deviceId = deviceId, qrCode = qr))
			qrCode.qrCode = qr
			qrCode.updatedAt = LocalDateTime.now()
			qrCode.updatedBy = device.user?.email
			qrCode.createdBy = device.user?.email
			qrCodeRepository.save(qrCode)
			messagingTemplate.convertAndSendToUser(device.user!!.email, "/queue/qr", data)
		}
	}
}