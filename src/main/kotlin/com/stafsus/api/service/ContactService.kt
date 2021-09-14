package com.stafsus.api.service

import com.stafsus.api.dto.WaSyncContactDto

interface ContactService {
	fun syncFromWhatsApp(waContactDto: WaSyncContactDto)
}