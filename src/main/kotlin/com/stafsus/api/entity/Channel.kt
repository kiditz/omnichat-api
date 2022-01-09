package com.stafsus.api.entity

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import org.hibernate.Hibernate
import javax.persistence.*

@Entity
@Table
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator::class,
	property = "id"
)
data class Channel(
	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
	@Column(nullable = false, length = 100)
	var name: String,
	@Column(nullable = false, length = 8)
	var deviceId: String,
	var imageUrl: String? = null,
) : Auditable() {
	@ManyToOne(cascade = [CascadeType.ALL])
	@JoinColumn(name = "company_id")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	var company: Company? = null

	@ManyToOne(cascade = [CascadeType.ALL])
	@JoinColumn(name = "product_id")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	var product: Product? = null

	@OneToOne(mappedBy = "channel", cascade = [CascadeType.ALL])
	var whatsApp: WhatsAppChannel? = null

	@OneToOne(mappedBy = "channel")
	var telegram: TelegramChannel? = null

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
		other as Channel

		return id != null && id == other.id
	}

	override fun hashCode(): Int = javaClass.hashCode()
}
