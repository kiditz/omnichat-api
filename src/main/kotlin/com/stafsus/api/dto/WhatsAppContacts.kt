package com.stafsus.api.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.stafsus.api.entity.Contact

@JsonIgnoreProperties(ignoreUnknown = true)
data class WhatsAppContacts(
	var deviceId: String? = null,
	var contacts: List<Contact> = listOf(),
)
