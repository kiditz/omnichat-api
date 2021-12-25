package com.stafsus.api.utils

import com.fasterxml.jackson.databind.util.StdConverter
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

class UnixToInstantConverter : StdConverter<Long, OffsetDateTime>() {
	override fun convert(value: Long?): OffsetDateTime? {
		if (value != null && value > 0) {
			return value.let { Instant.ofEpochSecond(it).atOffset(ZoneOffset.UTC) }
		}
		return null
	}
}