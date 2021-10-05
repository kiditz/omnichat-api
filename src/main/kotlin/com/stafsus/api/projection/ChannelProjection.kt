package com.stafsus.api.projection

import com.stafsus.api.entity.ChannelStatus
import com.stafsus.api.entity.DeviceStatus
import com.stafsus.api.entity.ProductType
import org.springframework.beans.factory.annotation.Value

interface ChannelProjection {
	fun getId(): Long
	fun getName(): String
	fun getImageUrl(): String
	@Value("#{target.company?.name}")
	fun getCompany(): String?
	@Value("#{target.product?.type}")
	fun getProductType(): ProductType?
	fun getDeviceId(): String
	@Value("#{target.whatsApp?.qrCode}")
	fun getWhatsappQr(): String?
	@Value("#{target.whatsApp?.phone}")
	fun getWhatsappPhone(): String?
	@Value("#{target.whatsApp?.status}")
	fun getWhatsappStatus(): ChannelStatus?
	@Value("#{target.whatsApp?.deviceStatus}")
	fun getWhatsappDeviceStatus(): DeviceStatus?
	@Value("#{target.telegram?.botName}")
	fun getTelegramBotName(): String?
	@Value("#{target.telegram?.botUsername}")
	fun getTelegramBotUsername(): String?
	@Value("#{target.telegram?.status}")
	fun getTelegramStatus(): ChannelStatus?
	@Value("#{target.telegram?.deviceStatus}")
	fun getTelegramDeviceStatus(): DeviceStatus?


}