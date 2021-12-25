package com.stafsus.api.projection

data class ChannelProjection(
	val id: Long,
	val deviceId: String,
	val name: String,
	val imageUrl: String? = null,

//	fun getId(): Long
//	fun getDeviceId(): String
//	fun getName(): String
//	fun getImageUrl(): String
//
//	@Value("#{target.company?.name}")
//	fun getCompany(): String?
//
//	@Value("#{target.product?.type}")
//	fun getProductType(): ProductType?
//
//	@Value("#{target.whatsApp?.qrCode}")
//	fun getWhatsappQr(): String?
//
//	@Value("#{target.whatsApp?.phone}")
//	fun getWhatsappPhone(): String?
//
//	@Value("#{target.whatsApp?.status}")
//	fun getWhatsappStatus(): ChannelStatus?
//
//	@Value("#{target.whatsApp?.deviceStatus}")
//	fun getWhatsappDeviceStatus(): DeviceStatus?
//
//	@Value("#{target.telegram?.botName}")
//	fun getTelegramBotName(): String?
//
//	@Value("#{target.telegram?.botUsername}")
//	fun getTelegramBotUsername(): String?
//
//	@Value("#{target.telegram?.status}")
//	fun getTelegramStatus(): ChannelStatus?
//
//	@Value("#{target.telegram?.deviceStatus}")
//	fun getTelegramDeviceStatus(): DeviceStatus?
)