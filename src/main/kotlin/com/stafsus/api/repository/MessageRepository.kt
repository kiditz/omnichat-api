package com.stafsus.api.repository

import com.stafsus.api.entity.Message
import org.springframework.data.jpa.repository.JpaRepository
import java.time.OffsetDateTime
import java.util.*

interface MessageRepository : JpaRepository<Message, String> {
	fun findByFromAndTimestamp(from: String?, timestamp: OffsetDateTime?): Optional<Message>
}