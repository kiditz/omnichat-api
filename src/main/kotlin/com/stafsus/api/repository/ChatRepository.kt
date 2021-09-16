package com.stafsus.api.repository

import com.stafsus.api.entity.Chat
import com.stafsus.api.entity.Contact
import com.stafsus.api.entity.UserPrincipal
import com.stafsus.api.projection.ChatProjection
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ChatRepository : JpaRepository<Chat, String> {
	fun findByIdIn(ids: List<String?>): List<Chat>
	fun findByUserId(userId: Long, pageable: Pageable): Page<ChatProjection>
}