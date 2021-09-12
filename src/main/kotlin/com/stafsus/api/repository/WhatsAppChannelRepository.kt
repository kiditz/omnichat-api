package com.stafsus.api.repository

import com.stafsus.api.entity.WhatsAppChannel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface WhatsAppChannelRepository : JpaRepository<WhatsAppChannel, Long> {
	fun findByChannelId(channelId: Long): Optional<WhatsAppChannel>
}