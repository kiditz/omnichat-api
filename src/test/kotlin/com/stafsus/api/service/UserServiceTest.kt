package com.stafsus.api.service

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

internal class UserServiceTest {
	private val log = LoggerFactory.getLogger(javaClass)
	@Test
	fun save() {
		val encoder = BCryptPasswordEncoder()
		log.info(encoder.encode("admin@1234"));
	}
}