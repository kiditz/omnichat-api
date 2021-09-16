package com.stafsus.api.service

import com.stafsus.api.dto.WaSyncChatDto
import com.stafsus.api.entity.UserPrincipal
import com.stafsus.api.projection.ChatProjection
import org.springframework.data.domain.Page

interface ChatService {
	fun syncFromWhatsApp(waChatDto: WaSyncChatDto)
	fun findChats(page: Int, size: Int, userPrincipal: UserPrincipal): Page<ChatProjection>
}