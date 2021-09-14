package com.stafsus.api.dto

import com.stafsus.api.entity.Contact

data class WaSyncContactDto(
	var deviceId: String? = null,
	var contacts: List<Contact> = listOf(),
)
