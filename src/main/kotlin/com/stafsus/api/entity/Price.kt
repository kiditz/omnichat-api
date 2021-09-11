package com.stafsus.api.entity

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.OrderBy
import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table
data class Price(
	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
	@Column(nullable = false, length = 60)
	var name: String,
	@Column(nullable = false, length = 60)
	var price: BigDecimal,
	@Column(name = "access_time", nullable = false)
	var accessTime: Long,
	@Enumerated(EnumType.STRING)
	@Column(name = "price_type")
	var priceType: PriceType? = null,
	@Column(name = "agent_amount")
	var agentAmount: Int? = null,
	@Column(name = "channel_amount")
	var channelAmount: Int? = null,
	var monthlyActiveVisitor: Int? = null,
	@ManyToOne
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@JoinColumn(name = "product_id")
	var product: Product? = null,
	@OneToMany(mappedBy = "price", cascade = [CascadeType.ALL])
	@OrderBy(clause = "id")
	var descriptions: List<PriceDescription>? = null
) : Auditable() {
	companion object {
		const val TRIAL = "TRIAL"
	}
}

