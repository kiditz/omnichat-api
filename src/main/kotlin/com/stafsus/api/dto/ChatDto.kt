package com.stafsus.api.dto

data class ChatDto(
	val server: String? = null,
	val pinned: Boolean? = null,
	val unreadCount: Int? = null,
	val picture: String? = null,
	val number: String? = null,
	val archived: Boolean? = null,
	val isReadOnly: Boolean? = null,
	val name: String? = null,
	val muteExpiration: Int? = null,
	val isGroup: Boolean? = null,
	val isMuted: Boolean? = null,
	val serialized: String? = null,
	val timestamp: Int? = null
)

