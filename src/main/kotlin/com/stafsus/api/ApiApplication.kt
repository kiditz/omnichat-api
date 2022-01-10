package com.stafsus.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import java.util.*
import javax.annotation.PostConstruct

@SpringBootApplication
@EnableFeignClients
class ApiApplication

@PostConstruct
fun init() {
	TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
	println("Date ${Date()}")
}

fun main(args: Array<String>) {

	runApplication<ApiApplication>(*args)
}
