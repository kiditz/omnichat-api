package com.stafsus.waapi.service

import com.stafsus.waapi.service.dto.ResponseDto

interface DeviceApiService {
	fun getContacts(deviceId: String): ResponseDto
	fun getChat(deviceId: String): ResponseDto
	fun logout(deviceId: String): ResponseDto
}