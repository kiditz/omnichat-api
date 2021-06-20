package com.stafsus.waapi.service

import com.stafsus.waapi.config.RabbitConfig
import com.stafsus.waapi.entity.User
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service


@Service
class WaServiceImpl(
        private val rabbitTemplate: RabbitTemplate,
) : WaService {
    override fun sendDeviceToQueue(user: User, deviceId: String) {
        val result = mutableMapOf(
                "deviceId" to deviceId,
                "userId" to user.id
        )
        rabbitTemplate.convertAndSend(RabbitConfig.DEPLOY_RQ, result)
    }
}