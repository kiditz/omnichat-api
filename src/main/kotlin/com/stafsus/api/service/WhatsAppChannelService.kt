package com.stafsus.api.service

import com.stafsus.api.dto.WhatsAppChannelDto
import com.stafsus.api.entity.WhatsAppChannel
import java.util.*

interface WhatsAppChannelService {
	fun save(channelDto: WhatsAppChannelDto): Optional<WhatsAppChannel>
}