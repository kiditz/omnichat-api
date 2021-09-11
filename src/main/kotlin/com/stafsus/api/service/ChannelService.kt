package com.stafsus.api.service

import com.stafsus.api.dto.ChannelDto
import com.stafsus.api.entity.Channel
import com.stafsus.api.entity.UserPrincipal

interface ChannelService {
	fun install(channelDto: ChannelDto, userPrincipal: UserPrincipal): Channel
}