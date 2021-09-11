package com.stafsus.api.dto

import javax.validation.constraints.NotNull

data class TransactionDto(
	@NotNull
	val priceId: Long
)