package com.stafsus.api.repository

import com.stafsus.api.entity.Message
import org.springframework.data.jpa.repository.JpaRepository

interface MessageRepository : JpaRepository<Message, String> {
}