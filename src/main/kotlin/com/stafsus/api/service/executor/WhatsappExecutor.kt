package com.stafsus.api.service.executor

import com.stafsus.api.entity.Channel
import com.stafsus.api.entity.ChannelStatus
import com.stafsus.api.entity.DeviceStatus
import com.stafsus.api.repository.ChannelRepository
import com.stafsus.api.service.RabbitService
import org.springframework.stereotype.Service

@Service("UNOFFICIAL_WHATSAPP")
class WhatsappExecutor(
	private val channelRepository: ChannelRepository,
	private val rabbitService: RabbitService,
) : ChannelExecutor {
	override fun install(channel: Channel, map: Map<String, Any?>): Channel {
		channelRepository.saveAndFlush(channel)
		rabbitService.sendInstall(channel)
		return channel
	}

	override fun restart(channel: Channel): Channel {
		channel.whatsApp?.deviceStatus = DeviceStatus.PENDING
		channelRepository.saveAndFlush(channel)
		rabbitService.sendRestart(channel)
		return channel
	}

	override fun stop(channel: Channel): Channel {
		resetValue(channel)
		channelRepository.saveAndFlush(channel)
		rabbitService.sendStop(channel)
		return channel
	}

	private fun resetValue(channel: Channel) {
		channel.whatsApp?.deviceStatus = DeviceStatus.INACTIVE
		channel.whatsApp?.status = ChannelStatus.OFFLINE
		channel.whatsApp?.qrCode = null
		channel.whatsApp?.browserSession = null
		channel.whatsApp?.phone = null
		channel.whatsApp?.pushName = null
	}

}