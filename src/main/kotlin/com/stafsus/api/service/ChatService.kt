package com.stafsus.api.service

import com.stafsus.api.dto.WaSyncChatDto

interface ChatService {
	fun syncFromWhatsApp(waChatDto: WaSyncChatDto)
}