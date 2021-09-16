package com.stafsus.api.service

import com.stafsus.api.dto.WaSyncMessageDto

interface MessageService {
	fun syncFromWhatsApp(waMessageDto: WaSyncMessageDto)
}