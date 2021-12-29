package com.stafsus.api.repository

import com.stafsus.api.entity.Channel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ChannelRepository : JpaRepository<Channel, Long> {
	fun countByCompanyId(companyId: Long): Long
	fun findByProductIdAndCompanyId(productId: Long, companyId: Long, pageable: Pageable): Page<Channel>
	fun findByProductIdAndCompanyId(productId: Long, companyId: Long): List<Channel>
	fun findByDeviceId(deviceId: String): Optional<Channel>
}