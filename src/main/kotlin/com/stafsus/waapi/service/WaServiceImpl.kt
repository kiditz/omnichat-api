package com.stafsus.waapi.service

import com.stafsus.waapi.service.security.SshTransportConfigCallback
import com.stafsus.waapi.utils.Random
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FileUtils.deleteDirectory
import org.apache.logging.log4j.ThreadContext
import org.eclipse.jgit.api.Git
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


@Service
class WaServiceImpl(
        @Value("\${git.url}") val gitUrl: String,
        @Value("\${git.privateKey}") val privateKey: String,

        ) : WaService {
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    private val tracingId: String = "tracingId"
    override fun generateDevice(deviceId: String) {
        val destDir = Files.createTempDirectory("wa-integration").toFile()

        ThreadContext.put(tracingId, Random.string(5))
        log.info("Start compute new application with deviceId: $deviceId tracingId: [$tracingId] In : $destDir ")
        cloneRepository(destDir)
        buildDockerImage(destDir, deviceId)
        deleteDirectory(destDir)
        ThreadContext.remove(tracingId)
        log.info("End compute")
    }


    private fun cloneRepository(destDir: File) {
        if (FileUtils.isEmptyDirectory(destDir))
            Git.cloneRepository()
                    .setURI(gitUrl)
                    .setTransportConfigCallback(SshTransportConfigCallback(privateKey))
                    .setDirectory(destDir)
                    .call().use { result ->
                        log.info("Having repository: " + result.repository.directory)
                    }
    }

    private fun buildDockerImage(destDir: File, deviceId: String) {
        log.info("build docker image")
        val builder = ProcessBuilder()
        builder.directory(destDir)
        builder.redirectErrorStream(true)
        builder.command("docker", "build", ".", "-t", "wa:${deviceId}")
        val start = System.currentTimeMillis()
        log.info("Start Execute")
        val process = builder.start()
        inheritIO(process.inputStream, false)
        inheritIO(process.errorStream, true)
        process.waitFor()
        val end = System.currentTimeMillis()
        val formatter: NumberFormat = DecimalFormat("#0.00000")
        log.info("End Execute after ${formatter.format((end - start) / 1000.0)} seconds")
    }

    private fun inheritIO(src: InputStream, isError: Boolean) {
        Thread {
            val sc = Scanner(src)
            while (sc.hasNextLine()) {
                if (isError) {
                    log.error("Error > ${sc.nextLine()}")
                } else {
                    log.info("Run > ${sc.nextLine()}")
                }
            }
        }.start()
    }
}