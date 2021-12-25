package com.stafsus.api.service.executor

import com.stafsus.api.entity.Channel

interface ChannelExecutor {
	fun install(channel: Channel, map: Map<String, Any?>): Channel
	fun restart(channel: Channel): Channel
	fun stop(channel: Channel): Channel
}