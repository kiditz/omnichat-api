package com.stafsus.api.service

import com.stafsus.api.dto.MailMessageDto
import com.stafsus.api.entity.Channel
import com.stafsus.api.entity.ProductType

interface RabbitService {
	fun sendInstall(channel: Channel)
	fun sendRestart(channel: Channel)
	fun sendEmail(mailMessageDto: MailMessageDto)
	fun sendStop(channel: Channel)
}