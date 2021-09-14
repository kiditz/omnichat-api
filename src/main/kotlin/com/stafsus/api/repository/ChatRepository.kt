package com.stafsus.api.repository

import com.stafsus.api.entity.Chat
import com.stafsus.api.entity.Contact
import org.springframework.data.jpa.repository.JpaRepository

interface ChatRepository : JpaRepository<Chat, String> {
	fun findByIdIn(ids: List<String?>): List<Chat>
}