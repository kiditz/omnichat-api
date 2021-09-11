package com.stafsus.api.dto

import com.stafsus.api.entity.Channel
import org.apache.commons.lang3.RandomStringUtils
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

data class ChannelDto(
	@field:NotBlank
	var name: String? = null,
	var phone: String? = null,
	@field:NotNull
	@field:Positive
	var productId: Long? = null,
) {
	fun toEntity(): Channel {
		return Channel(
			name = name!!,
			phone = phone,
			deviceId = RandomStringUtils.randomAlphabetic(8).toLowerCase(),
			isOnline = false,
			isActive = false,
		)
	}
}
