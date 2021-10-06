package com.stafsus.api.dto

import com.stafsus.api.entity.Channel
import org.apache.commons.lang3.RandomStringUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.web.multipart.MultipartFile

data class ChannelDto(
	val name: String,
	val productId: Long,
	val telegramToken: String?,
	val facebookToken: String?,
	val instagramToken: String?,
	val file: MultipartFile?,
) {
	fun toEntity(): Channel {
		return Channel(
			name = name,
			deviceId = StringUtils.lowerCase(RandomStringUtils.randomAlphabetic(8)),
		)
	}
}
