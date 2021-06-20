package com.stafsus.waapi.service

import com.stafsus.waapi.entity.Role
import com.stafsus.waapi.entity.Status
import com.stafsus.waapi.entity.User
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
        waService.sendDeviceToQueue(User(
                email = "", password = "", role = Role.ROLE_ADMIN, authorities = setOf(), status = Status.ACTIVE, username = ""
        ), Random.string(5))
    }
}