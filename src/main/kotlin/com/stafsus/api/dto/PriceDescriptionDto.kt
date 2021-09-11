package com.stafsus.api.dto

import com.stafsus.api.entity.FieldType

data class PriceDescriptionDto(
	var field: String,
	var fieldType: FieldType,
	var fieldValue: String,
)