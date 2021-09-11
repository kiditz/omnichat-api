package com.stafsus.api.service

import com.stafsus.api.dto.midtrans.MidtransDto
import com.stafsus.api.dto.midtrans.MidtransNotification
import com.stafsus.api.entity.Transaction
import com.stafsus.api.entity.UserPrincipal

interface TransactionService {
	fun buyPackage(userPrincipal: UserPrincipal, priceId: Long): MidtransDto
	fun paymentNotification(notification: MidtransNotification): Transaction
}