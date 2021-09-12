package com.stafsus.api.repository

import com.stafsus.api.entity.Channel
import org.springframework.data.jpa.repository.JpaRepository

interface ChannelRepository : JpaRepository<Channel, Long> {
	fun countByUserId(userId: Long): Long
}