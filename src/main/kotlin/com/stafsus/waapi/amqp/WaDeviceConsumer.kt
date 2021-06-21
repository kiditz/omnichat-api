package com.stafsus.waapi.amqp

import com.stafsus.waapi.config.RabbitConfig
import com.stafsus.waapi.entity.DeviceInfo
import com.stafsus.waapi.service.WaDeviceService
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class WaDeviceConsumer(
        private val waDeviceService: WaDeviceService
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @RabbitListener(queues = [RabbitConfig.LOGS_Q])
    fun logs(data: Map<String, Any>) {
        log.info("Data Received : $data")
        val deviceId = data["deviceId"] as String
        val deviceInfo = data["deviceInfo"] as String
        waDeviceService.updateDeviceInfo(deviceId, DeviceInfo.valueOf(deviceInfo))

    }
}