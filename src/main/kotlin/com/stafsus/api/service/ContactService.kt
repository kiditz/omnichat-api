package com.stafsus.api.service

import com.stafsus.api.dto.WhatsAppContacts

interface ContactService {
	fun syncFromWhatsApp(waContacts: WhatsAppContacts)
}