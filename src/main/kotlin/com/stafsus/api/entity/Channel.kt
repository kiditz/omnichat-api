package com.stafsus.api.entity

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity
@Table
data class Channel(
	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
	@Column(nullable = false, length = 100)
	var name: String,
	@Column(nullable = false, length = 8)
	var deviceId: String,
	@Column(length = 20)
	var phone: String? = null,
	@Column(nullable = false)
	var isOnline: Boolean,
	@Column(nullable = false)
	var isActive: Boolean,
	@Column(nullable = false)
	var isPending: Boolean,
	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	var user: UserPrincipal? = null,
	@ManyToOne
	@JoinColumn(name = "product_id")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	var product: Product? = null,
	@OneToOne(mappedBy = "channel")
	var whatsApp: WhatsAppChannel? = null
) : Auditable()
