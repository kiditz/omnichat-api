package com.stafsus.waapi.service

import com.stafsus.waapi.utils.Random
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class WaServiceImplTest {
    @Autowired
    lateinit var waService: WaService

    @Test
    fun generateDevice() {
        waService.deployDevice(Random.string(5))
    }
}