package com.stafsus.api.dto.midtrans

import com.fasterxml.jackson.annotation.JsonProperty

data class MidtransRequest(

	@field:JsonProperty("item_details")
	val itemDetails: List<MidtransItems?>? = null,

	@field:JsonProperty("transaction_details")
	val transactionDetails: MidtransTransactionDetails? = null,
	@field:JsonProperty("custom_field1")
	val customField1: String? = null,
	@field:JsonProperty("custom_field2")
	val customField2: String? = null
)

data class MidtransTransactionDetails(

	@field:JsonProperty("gross_amount")
	val grossAmount: Int? = null,

	@field:JsonProperty("order_id")
	val orderId: String? = null
)

data class MidtransItems(

	@field:JsonProperty("quantity")
	val quantity: Int? = null,

	@field:JsonProperty("price")
	val price: Long? = null,

	@field:JsonProperty("name")
	val name: String? = null,

	@field:JsonProperty("merchant_name")
	val merchantName: String? = null,

	@field:JsonProperty("id")
	val id: String? = null,

	@field:JsonProperty("category")
	val category: String? = null,

	@field:JsonProperty("brand")
	val brand: String? = null
)
