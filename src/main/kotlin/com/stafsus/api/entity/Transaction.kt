package com.stafsus.api.entity

import org.hibernate.Hibernate
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
) : Auditable() {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
		other as Transaction

		return orderId == other.orderId
	}

	override fun hashCode(): Int = javaClass.hashCode()

	@Override
	override fun toString(): String {
		return this::class.simpleName + "(orderId = $orderId )"
	}
}
