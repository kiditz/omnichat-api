package com.stafsus.waapi.service

import com.stafsus.waapi.config.RabbitConfig
import com.stafsus.waapi.entity.User
import com.stafsus.waapi.entity.WaDevice
import com.stafsus.waapi.repository.WaDeviceRepository
import com.stafsus.waapi.service.security.SshTransportConfigCallback
import com.stafsus.waapi.utils.Random
import org.apache.commons.io.FileUtils
import org.apache.logging.log4j.ThreadContext
import org.eclipse.jgit.api.Git
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.time.LocalDateTime


@Service
class WaServiceImpl(
        @Value("\${app.device.period}") val period: Long,
        private val rabbitTemplate: RabbitTemplate,
        private val deviceRepository: WaDeviceRepository,
        private val resourceLoader: ResourceLoader
) : WaService {
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    private val tracingId: String = "tracingId"
    override fun deployDevice(user: User, deviceId: String) {
        saveDevice(user, deviceId)
    }

    private fun saveDevice(user: User, deviceId: String) {
        val startAt = LocalDateTime.now()
        val endAt = LocalDateTime.now().plusDays(period)
        val device = WaDevice(deviceId = deviceId, user = user, startAt = startAt, endAt = endAt)
        deviceRepository.save(device)
        rabbitTemplate.convertAndSend(RabbitConfig.DEPLOY, device)
    }

//    private fun writeDevice(deviceId: String) {
//        val destDir = File(cloneDir, deviceId)
//        if(!destDir.isDirectory)
//            destDir.mkdirs()
//        log.info("Start compute new application with deviceId: $deviceId tracingId: [$tracingId] In : $destDir ")
//        cloneRepository(destDir)
//        log.info("End compute")
//        val result = mutableMapOf<String, Any>(
//                "deviceId" to deviceId,
//                "directory" to destDir
//        )
//        rabbitTemplate.convertAndSend(RabbitConfig.DEPLOY, result)
//    }


//    private fun cloneRepository(destDir: File) {
//        val path = resourceLoader.getResource("classpath:id_rsa").file
//        val rsaStr = FileUtils.readFileToString(path, StandardCharsets.UTF_8)
//        if (FileUtils.isEmptyDirectory(destDir))
//            Git.cloneRepository()
//                    .setURI(gitUrl)
//                    .setTransportConfigCallback(SshTransportConfigCallback(rsaStr))
//                    .setDirectory(destDir)
//                    .call().use { result ->
//                        log.info("Having repository: " + result.repository.directory)
//                    }
//    }
}