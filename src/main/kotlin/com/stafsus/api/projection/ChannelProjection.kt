package com.stafsus.api.projection

import org.springframework.beans.factory.annotation.Value

interface ChannelProjection {
	fun getId(): Long
	fun getName(): String
	@Value("#{target.product.imageUrl}")
	fun getImageUrl(): String
	@Value("#{target.company?.name}")
	fun getCompany(): String?
	fun getDeviceId(): String
	@Value("#{target.whatsApp?.qrCode}")
	fun getQr(): String?
	@Value("#{target.whatsApp?.phone != target.phone ? target.phone : target.whatsApp?.phone}")
	fun getPhone(): String?
	fun getIsOnline(): Boolean
	fun getIsActive(): Boolean
	fun getIsPending(): Boolean
}