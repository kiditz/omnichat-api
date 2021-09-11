package com.stafsus.api.entity

import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table(name = "transaction")
data class Transaction(
	@Id
	@Column(name = "order_id", nullable = false, length = 255)
	var orderId: String,
	@Column(nullable = false)
	var grossAmount: BigDecimal,
	@Enumerated(EnumType.STRING)
	var status: TransactionStatus? = null,
	@OneToMany(mappedBy = "transaction", cascade = [CascadeType.ALL])
	var items: List<ItemDetails>? = null,
) : Auditable()
