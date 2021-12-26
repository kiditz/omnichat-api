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
@Table
data class Company(
	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
	@Column(nullable = false, length = 100)
	var name: String,
	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	var user: UserPrincipal,
	@OneToOne
	@JoinColumn(name = "industry_id")
	var industry: Industry,
	@OneToOne
	@JoinColumn(name = "quota_id", nullable = true)
	var quota: Quota,
) : Auditable() {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
		other as Company

		return id != null && id == other.id
	}

	override fun hashCode(): Int = javaClass.hashCode()

	@Override
	override fun toString(): String {
		return this::class.simpleName + "(id = $id )"
	}
}
