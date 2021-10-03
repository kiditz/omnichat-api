package com.stafsus.api.repository

import com.stafsus.api.entity.Channel
import com.stafsus.api.projection.ChannelProjection
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ChannelRepository : JpaRepository<Channel, Long> {
	fun countByCompanyId(companyId: Long): Long
	fun findByCompanyId(companyId: Long, pageable: Pageable): Page<ChannelProjection>
	fun findByDeviceId(deviceId: String): Optional<Channel>
}