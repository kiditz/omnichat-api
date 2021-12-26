package com.stafsus.api.entity

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import org.hibernate.Hibernate
import javax.persistence.*

@Entity
@Table(name = "user_principal")
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator::class,
	property = "id"
)
data class UserPrincipal(
	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
	@Column(nullable = false, length = 60)
	var email: String,
	@Column(nullable = false, length = 100)
	var name: String,
	@Column(nullable = false, length = 60)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	var password: String,
	@Column(nullable = false)
	var imageUrl: String? = null,
	@Column(nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	var status: Status,
	@Column(nullable = false)
	var isVerified: Boolean,
) : Auditable() {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
		other as UserPrincipal

		return id != null && id == other.id
	}

	override fun hashCode(): Int = javaClass.hashCode()
	@Override
	override fun toString(): String {
		return this::class.simpleName + "(id = $id )"
	}
}
