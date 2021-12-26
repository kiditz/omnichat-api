package com.stafsus.api.entity

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import org.hibernate.Hibernate
import javax.persistence.*

@Entity
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator::class,
	property = "id"
)
@Table(name = "telegram_channel")
data class TelegramChannel(
	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
	@Column(length = 255)
	var botName: String? = null,
	var botUsername: String? = null,
	var botToken: String? = null,
	@Enumerated(EnumType.STRING)
	var status: ChannelStatus = ChannelStatus.PENDING,
	@Enumerated(EnumType.STRING)
	var deviceStatus: DeviceStatus = DeviceStatus.INACTIVE,
	@OneToOne
	@JoinColumn(name = "channel_id")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	var channel: Channel? = null,
) : Auditable() {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
		other as TelegramChannel

		return id != null && id == other.id
	}

	override fun hashCode(): Int = javaClass.hashCode()

	@Override
	override fun toString(): String {
		return this::class.simpleName + "(id = $id )"
	}
}
