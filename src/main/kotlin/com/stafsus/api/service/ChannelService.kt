package com.stafsus.api.service

import com.stafsus.api.dto.ChannelDto
import com.stafsus.api.dto.ControlChannelDto
import com.stafsus.api.entity.Channel
import com.stafsus.api.entity.UserPrincipal
import org.springframework.data.domain.Page

interface ChannelService {
	fun addChannel(channelDto: ChannelDto, userPrincipal: UserPrincipal): Channel
	fun controlChannel(control: ControlChannelDto, userPrincipal: UserPrincipal): Channel
	fun findChannels(productId: Long, page: Int, size: Int): Page<Channel>
}