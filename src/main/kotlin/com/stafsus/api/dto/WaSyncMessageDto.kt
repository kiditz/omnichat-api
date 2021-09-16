package com.stafsus.api.dto

import com.stafsus.api.entity.Contact
import com.stafsus.api.entity.Message

data class WaSyncMessageDto(
	var deviceId: String,
	var message: Message,
	var contact: Contact? = null,
)
