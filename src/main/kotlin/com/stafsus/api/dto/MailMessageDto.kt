package com.stafsus.api.dto

data class MailMessageDto(
	val to: String,
	val subject: String,
	val cc: String? = null,
	val bcc: String? = null,
	val message: Any,
	val template: String,
	val isHtml: Boolean = false
)