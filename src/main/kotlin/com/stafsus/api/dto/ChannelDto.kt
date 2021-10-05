package com.stafsus.api.dto

import com.stafsus.api.entity.Channel
import org.apache.commons.lang3.RandomStringUtils
import org.apache.commons.lang3.StringUtils
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Positive

data class ChannelDto(
	@field:NotBlank
	var name: String? = null,
	@field:NotNull
	@field:Positive
	var productId: Long? = null,
	var botToken: String? = null,
) {
	fun toEntity(): Channel {
		return Channel(
			name = name!!,
			deviceId = StringUtils.lowerCase(RandomStringUtils.randomAlphabetic(8)),
		)
	}
}
