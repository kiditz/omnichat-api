package com.stafsus.api.dto

import com.stafsus.api.entity.Chat

data class WaSyncChatDto(
	var deviceId: String? = null,
	var chats: List<Chat> = listOf(),
)
