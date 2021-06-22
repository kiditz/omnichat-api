package com.stafsus.waapi.service

import com.stafsus.waapi.config.RabbitConfig
import com.stafsus.waapi.constant.MessageKey
import com.stafsus.waapi.entity.DeviceInfo
import com.stafsus.waapi.entity.DeviceStatus
import com.stafsus.waapi.entity.User
import com.stafsus.waapi.entity.WaDevice
import com.stafsus.waapi.exception.ValidationException
import com.stafsus.waapi.feign.WaDeviceClient
import com.stafsus.waapi.repository.UserRepository
import com.stafsus.waapi.repository.WaDeviceRepository
import com.stafsus.waapi.service.dto.ResponseDto
import com.stafsus.waapi.service.dto.DeviceDto
import com.stafsus.waapi.utils.Random
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.net.URI
import java.security.Principal
import java.time.LocalDateTime
import javax.transaction.Transactional


@Service
class WaDeviceServiceImpl(
        private val rabbitTemplate: RabbitTemplate,
        private val deviceRepository: WaDeviceRepository,
        private val userRepository: UserRepository,
        private val waDeviceClient: WaDeviceClient,
        private val translateService: TranslateService,
        @Value("\${app.device.period}") val period: Long,
        @Value("\${spring.profiles.active}") val appProfile: String,
        @Value("\${feign.client.config.wa-device.url}") val defaultUrl: String,

        ) : WaDeviceService {
    private val log = LoggerFactory.getLogger(javaClass)
    override fun getQrCode(deviceId: String): ResponseDto {
        val device = deviceRepository.findByDeviceId(deviceId).orElseThrow { ValidationException(MessageKey.INVALID_DEVICE_ID) }
        if (device.deviceInfo != DeviceInfo.ACTIVE) {
            throw ValidationException(MessageKey.INACTIVE_DEVICE)
        }
        var url = defaultUrl
        if (appProfile != "dev") {
            url = "http://${device.deviceId}"
        }
        val qrCodeResponse = waDeviceClient.getQrCode(URI.create(url))
        return qrCodeResponse.copy(message = translateService.toLocale(qrCodeResponse.message as String))
    }


    @Transactional
    override fun install(principal: Principal): DeviceDto {
        val user = userRepository.findByEmail(principal.name).orElseThrow { ValidationException(MessageKey.USER_NOT_FOUND) }
        return install(user)
    }

    @Transactional
    override fun install(user: User): DeviceDto {
        val startAt = LocalDateTime.now()
        val endAt = LocalDateTime.now().plusDays(period)
        val device = WaDevice(deviceId = Random.stringLowerOnly(8), startAt = startAt, endAt = endAt, deviceStatus = DeviceStatus.PHONE_OFFLINE)
        device.user = user
        deviceRepository.save(device)
        log.info("Install Device :${device.deviceId}")
        val result = mutableMapOf(
                "deviceId" to device.deviceId,
                "deviceInfo" to DeviceInfo.ON_INSTALL
        )
        rabbitTemplate.convertAndSend(RabbitConfig.INSTALL_EX, RabbitConfig.INSTALL_RK, result)
        return DeviceDto(deviceId = device.deviceId, accessedBy = user.email)
    }

    override fun uninstall(deviceId: String, email: String) {
        log.info("Uninstall Device :${deviceId}:${email}")
        deviceRepository.findByDeviceIdAndUserEmail(deviceId, email).orElseThrow { ValidationException(MessageKey.INVALID_DEVICE_ID) }
        val result = mutableMapOf(
                "deviceId" to deviceId,
                "deviceInfo" to DeviceInfo.ON_UNINSTALLED
        )
        rabbitTemplate.convertAndSend(RabbitConfig.UNINSTALL_EX, RabbitConfig.UNINSTALL_RK, result)
    }

    override fun restart(deviceId: String, email: String) {
        log.info("Restart Device :${deviceId}:${email}")
        deviceRepository.findByDeviceIdAndUserEmail(deviceId, email).orElseThrow { ValidationException(MessageKey.INVALID_DEVICE_ID) }
        val result = mutableMapOf(
                "deviceId" to deviceId,
                "deviceInfo" to DeviceInfo.ON_RESTART
        )
        rabbitTemplate.convertAndSend(RabbitConfig.RESTART_EX, RabbitConfig.RESTART_RK, result)
    }

    override fun updateDeviceInfo(deviceId: String, deviceInfo: DeviceInfo) {
        val device = deviceRepository.findByDeviceId(deviceId).orElseThrow { ValidationException(MessageKey.INVALID_DEVICE_ID) }
        device.deviceInfo = deviceInfo
        deviceRepository.save(device)
    }
}