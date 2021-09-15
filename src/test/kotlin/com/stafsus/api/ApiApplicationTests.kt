package com.stafsus.api

import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.time.Instant

//@SpringBootTest
class ApiApplicationTests {
	private val log = LoggerFactory.getLogger(javaClass)

	@Test
	fun contextLoads() {
		log.info("Res:{}", Instant.ofEpochMilli(1631654136))
	}

}
