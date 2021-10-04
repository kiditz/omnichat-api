package com.stafsus.api.service

import com.stafsus.api.dto.ChannelDto
import com.stafsus.api.entity.Channel
import com.stafsus.api.entity.UserPrincipal
import com.stafsus.api.projection.ChannelProjection
import org.springframework.data.domain.Page

interface ChannelService {
	fun install(channelDto: ChannelDto, userPrincipal: UserPrincipal): Channel
	fun restart(deviceId: String, userPrincipal: UserPrincipal): Channel
	fun findChannels(productId: Long, page: Int, size: Int): Page<ChannelProjection>
}