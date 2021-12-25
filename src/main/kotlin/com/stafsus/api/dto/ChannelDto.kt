package com.stafsus.api.dto

import com.stafsus.api.entity.Channel
import org.apache.commons.lang3.RandomStringUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.web.multipart.MultipartFile
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

data class ChannelDto(
	@NotNull
	@NotBlank
	val name: String,
	@NotNull
	@Positive
	val productId: Long,
	val telegramToken: String?,
	val facebookToken: String?,
	val instagramToken: String?,
	var file: MultipartFile?,
) {
	fun toEntity(): Channel {
		return Channel(
			name = name,
			deviceId = StringUtils.lowerCase(RandomStringUtils.randomAlphabetic(8)),
		)
	}
}
