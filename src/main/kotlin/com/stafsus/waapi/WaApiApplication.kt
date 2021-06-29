package com.stafsus.waapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.scheduling.annotation.EnableScheduling

@EnableFeignClients
@SpringBootApplication
@EnableScheduling
class WaApiApplication

fun main(args: Array<String>) {
	runApplication<WaApiApplication>(*args)

}
