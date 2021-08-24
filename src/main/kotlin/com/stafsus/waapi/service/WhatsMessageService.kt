package com.stafsus.waapi.service

import com.stafsus.waapi.service.dto.ResponseDto

interface WhatsMessageService {
	fun getContacts(deviceId: String): ResponseDto
	fun getChat(deviceId: String): ResponseDto
	fun getChatDetail(deviceId: String, chatId: String): ResponseDto
	fun downloadMedia(deviceId: String, chatId: String, messageId: String): ResponseDto
	fun getInfo(deviceId: String): ResponseDto
}