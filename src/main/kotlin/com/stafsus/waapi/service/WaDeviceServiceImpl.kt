package com.stafsus.waapi.service

import com.stafsus.waapi.config.RabbitConfig
import com.stafsus.waapi.constant.MessageKey
import com.stafsus.waapi.entity.DeviceInfo
import com.stafsus.waapi.entity.DeviceStatus
import com.stafsus.waapi.entity.User
import com.stafsus.waapi.entity.WaDevice
import com.stafsus.waapi.exception.ValidationException
import com.stafsus.waapi.repository.WaDeviceRepository
import com.stafsus.waapi.utils.Random
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import javax.transaction.Transactional


@Service
class WaDeviceServiceImpl(
        private val rabbitTemplate: RabbitTemplate,
        private val deviceRepository: WaDeviceRepository,
        @Value("\${app.device.period}") val period: Long,
) : WaDeviceService {
    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    override fun install(user: User) {
        val startAt = LocalDateTime.now()
        val endAt = LocalDateTime.now().plusDays(period)
        val device = WaDevice(deviceId = Random.stringLowerOnly(8), startAt = startAt, endAt = endAt, deviceStatus = DeviceStatus.PHONE_OFFLINE)
        device.user = user
        deviceRepository.save(device)
        log.info("Install Device :${device.deviceId}")
        val result = mutableMapOf(
                "deviceId" to device.deviceId,
                "status" to DeviceInfo.ON_INSTALL
        )
        rabbitTemplate.convertAndSend(RabbitConfig.INSTALL_EX, RabbitConfig.INSTALL_RK, result)
    }

    override fun uninstall(deviceId: String, email: String) {
        log.info("Uninstall Device :${deviceId}:${email}")
        deviceRepository.findByDeviceIdAndUserEmail(deviceId, email).orElseThrow { ValidationException(MessageKey.INVALID_DEVICE_ID) }
        val result = mutableMapOf(
                "deviceId" to deviceId,
                "info" to DeviceInfo.ON_UNINSTALLED
        )
        rabbitTemplate.convertAndSend(RabbitConfig.UNINSTALL_EX, RabbitConfig.UNINSTALL_RK, result)
    }

    override fun restart(deviceId: String, email: String) {
        log.info("Restart Device :${deviceId}:${email}")
        deviceRepository.findByDeviceIdAndUserEmail(deviceId, email).orElseThrow { ValidationException(MessageKey.INVALID_DEVICE_ID) }
        val result = mutableMapOf(
                "deviceId" to deviceId,
                "status" to DeviceInfo.ON_RESTART
        )
        rabbitTemplate.convertAndSend(RabbitConfig.RESTART_EX, RabbitConfig.RESTART_RK, result)
    }
}