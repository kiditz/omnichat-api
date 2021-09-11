package com.stafsus.api.dto.midtrans

import com.fasterxml.jackson.annotation.JsonProperty

data class MidtransNotification(

	@field:JsonProperty("transaction_id")
	val transactionId: String? = null,

	@field:JsonProperty("status_message")
	val statusMessage: String? = null,

	@field:JsonProperty("fraud_status")
	val fraudStatus: String? = null,

	@field:JsonProperty("approval_code")
	val approvalCode: String? = null,

	@field:JsonProperty("transaction_status")
	val transactionStatus: String? = null,

	@field:JsonProperty("status_code")
	val statusCode: String? = null,

	@field:JsonProperty("signature_key")
	val signatureKey: String? = null,

	@field:JsonProperty("eci")
	val eci: String? = null,

	@field:JsonProperty("merchant_id")
	val merchantId: String? = null,

	@field:JsonProperty("gross_amount")
	val grossAmount: String? = null,

	@field:JsonProperty("card_type")
	val cardType: String? = null,

	@field:JsonProperty("payment_type")
	val paymentType: String? = null,

	@field:JsonProperty("bank")
	val bank: String? = null,

	@field:JsonProperty("masked_card")
	val maskedCard: String? = null,

	@field:JsonProperty("transaction_time")
	val transactionTime: String? = null,

	@field:JsonProperty("channel_response_code")
	val channelResponseCode: String? = null,

	@field:JsonProperty("currency")
	val currency: String? = null,

	@field:JsonProperty("order_id")
	val orderId: String? = null,

	@field:JsonProperty("channel_response_message")
	val channelResponseMessage: String? = null
)

class MidtransTransactionStatus {
	companion object {
		const val PENDING = "pending"
		const val CAPTURE = "capture"
		const val SETTLEMENT = "settlement"
		const val DENY = "deny"
		const val CANCEL = "cancel"
		const val EXPIRE = "expire"
		const val PARTIAL_REFUND = "partial_refund"
		const val REFUND = "refund"
	}
}

class MidtransFraudStatus {
	companion object {
		const val ACCEPT = "accept"
		const val DENY = "deny"
		const val CHALLENGE = "challenge"
	}
}