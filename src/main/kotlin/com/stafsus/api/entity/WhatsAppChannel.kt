package com.stafsus.api.entity

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity
@Table(name = "whatsapp_channel")
data class WhatsAppChannel(
	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
	@Column(length = 255)
	var pushName: String? = null,
	@Column(length = 20)
	var phone: String? = null,
	var browserSession: String? = null,
	var qrCode: String? = null,
	@OneToOne
	@JoinColumn(name = "channel_id")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	var channel: Channel? = null,
) : Auditable()
