package com.stafsus.api.repository

import com.stafsus.api.entity.Channel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ChannelRepository : JpaRepository<Channel, Long> {
	fun countByUserId(userId: Long): Long
	fun findByDeviceId(deviceId: String): Optional<Channel>
}