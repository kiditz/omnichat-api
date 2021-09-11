package com.stafsus.api.dto

import com.stafsus.api.entity.Tag
import com.stafsus.api.entity.TagType

data class TagDto(
	var name: String,
	var type: TagType,
	var color: String,
) {
	fun toEntity(): Tag {
		return Tag(
			name = name,
			type = type,
			color = color
		)
	}
}