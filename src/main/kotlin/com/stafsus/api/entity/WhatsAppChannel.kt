package com.stafsus.api.entity

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import javax.persistence.*

@Entity
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator::class,
	property = "id"
)
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
	@Enumerated(EnumType.STRING)
	var status: ChannelStatus = ChannelStatus.PENDING,
	@Enumerated(EnumType.STRING)
	var deviceStatus: DeviceStatus = DeviceStatus.INACTIVE,
	@OneToOne
	@JoinColumn(name = "channel_id")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	var channel: Channel? = null,
) : Auditable()
