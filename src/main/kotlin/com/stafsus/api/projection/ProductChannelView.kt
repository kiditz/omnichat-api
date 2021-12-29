package com.stafsus.api.projection

data class ProductChannelView(
	val id: Long,
	val name: String,
	val imageUrl: String,
	val channels: List<ChannelProductView>
)

data class ChannelProductView(
	val id: Long,
	val name: String,
	val imageUrl: String,
)