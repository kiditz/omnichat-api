package com.stafsus.waapi.service

import com.stafsus.waapi.entity.WaDevice
import com.stafsus.waapi.utils.Random
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class WaServiceImplTest {
    @Autowired
    lateinit var waService: WaService

    @Test
    fun generateDevice() {
        waService.generateDevice(Random.string(5))
    }
}