package com.stafsus.waapi.amqp

import com.stafsus.waapi.config.RabbitConfig
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class WaDeviceConsumer {
    private val log = LoggerFactory.getLogger(javaClass)

    @RabbitListener(queues = [RabbitConfig.LOGS_Q])
    fun logs(data: Map<String, Any>) {

        log.info("Data Received : $data")
    }
}