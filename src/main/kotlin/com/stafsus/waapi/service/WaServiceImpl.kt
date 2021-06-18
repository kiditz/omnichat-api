package com.stafsus.waapi.service

import com.stafsus.waapi.utils.Random
import org.apache.commons.io.FileUtils
import org.apache.logging.log4j.ThreadContext
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.io.InputStream
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


@Service
class WaServiceImpl(
    @Value("\${git.url}") val gitUrl: String,
    @Value("\${git.username}") val gitUsername: String,
    @Value("\${git.password}") val gitPassword: String,
) : WaService {
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    private val tracingId: String = "tracingId"
    override fun generateDevice(deviceId: String) {
        val destDir = File(System.getProperty("java.io.tmpdir"), "wa-integration")
        ThreadContext.put(tracingId, Random.string(5))
        log.info("Start compute new application with deviceId: $deviceId tracingId: [$tracingId] In : $destDir ")
        cloneRepository(destDir)
        buildDockerImage(destDir, deviceId)

        ThreadContext.remove(tracingId)
        log.info("End compute")
    }


    private fun cloneRepository(destDir: File) {
        val credentialsProvider = UsernamePasswordCredentialsProvider(gitUsername, gitPassword)
        if (!destDir.exists() || FileUtils.isEmptyDirectory(destDir)) {
            log.info("start clone repository")
            Git.cloneRepository()
                .setURI(gitUrl)
                .setCredentialsProvider(credentialsProvider)
                .setDirectory(destDir).call()
            log.info("success clone repository")
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