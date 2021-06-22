package com.stafsus.waapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootApplication
class WaApiApplication

fun main(args: Array<String>) {
	runApplication<WaApiApplication>(*args)
}
