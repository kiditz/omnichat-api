package com.stafsus.api.entity

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table(name = "item_details")
data class ItemDetails(
	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
	@Column(nullable = false)
	val name: String,
	@Column(name = "merchant_name")
	val merchantName: String,
	@Column(nullable = false)
	val quantity: Long,
	@ManyToOne
	@JoinColumn(name = "price_id")
	val price: Price? = null,
	@ManyToOne
	@JoinColumn(name = "order_id")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	val transaction: Transaction
) : Auditable()
