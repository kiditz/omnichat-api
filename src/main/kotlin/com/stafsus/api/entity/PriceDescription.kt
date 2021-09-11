package com.stafsus.api.entity

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity
@Table
data class PriceDescription(
	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
	@Column(name = "field", nullable = false)
	var field: String,
	@Enumerated(EnumType.STRING)
	@Column(name = "field_type", nullable = false)
	var fieldType: FieldType,
	@Column(name = "field_value", nullable = false)
	var fieldValue: String,
	@ManyToOne
	@JoinColumn(name = "price_id")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	var price: Price? = null
) : Auditable()