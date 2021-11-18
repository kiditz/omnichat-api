package com.stafsus.api.service

import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

internal class UserServiceTest {
	private val log = LoggerFactory.getLogger(javaClass)
	@Test
	fun save() {
		val encoder = BCryptPasswordEncoder()
		log.info(encoder.encode("maudy@12345"));
	}
}