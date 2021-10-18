package com.stafsus.api.service

import com.stafsus.api.dto.WhatsAppChannelDto
import com.stafsus.api.entity.WhatsAppChannel
import com.stafsus.api.repository.ChannelRepository
import com.stafsus.api.repository.WhatsAppChannelRepository
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class WhatsAppChannelServiceImpl(
	private val whatsAppChannelRepository: WhatsAppChannelRepository,
	private val channelRepository: ChannelRepository,
	private val simpMessagingTemplate: SimpMessagingTemplate
) : WhatsAppChannelService {

	@Transactional
	override fun save(channelDto: WhatsAppChannelDto): Optional<WhatsAppChannel> {
		val channelOpt = channelRepository.findByDeviceId(channelDto.deviceId!!)
		if (channelOpt.isPresent) {
			val channel = channelOpt.get()
			val waChannel = whatsAppChannelRepository.findByChannelId(channel.id!!).orElseGet { WhatsAppChannel() }
			waChannel.browserSession = channelDto.browserSession ?: waChannel.browserSession
			waChannel.phone = channelDto.phone ?: waChannel.phone
			waChannel.pushName = channelDto.pushName ?: waChannel.pushName
			waChannel.qrCode = channelDto.qrCode ?: waChannel.qrCode
			waChannel.channel = channel
			waChannel.status = channelDto.status ?: waChannel.status
			waChannel.deviceStatus = channelDto.deviceStatus ?: waChannel.deviceStatus
			channel.whatsApp = waChannel
			simpMessagingTemplate.convertAndSend("/topic/channel.${channel.id}", channel)
			return Optional.of(waChannel)
		}
		return Optional.empty()
	}

}