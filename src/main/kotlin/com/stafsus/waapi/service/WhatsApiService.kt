package com.stafsus.waapi.service

import com.stafsus.waapi.service.dto.ResponseDto

interface WhatsApiService {
	fun getContacts(deviceId: String): ResponseDto
	fun getChat(deviceId: String): ResponseDto
	fun getChatDetail(deviceId: String, chatId: String): ResponseDto
}