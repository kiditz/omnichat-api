package com.stafsus.api.entity

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.Hibernate
import javax.persistence.*

@Entity
@Table(name = "department")
data class Department(
	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
	@Column(nullable = false, length = 60)
	var name: String,
	@Column(nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	var status: Status,
	@OneToOne
	@JoinColumn(name = "company_id")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	var company: Company? = null,
) : Auditable() {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
		other as Department

		return id != null && id == other.id
	}

	override fun hashCode(): Int = javaClass.hashCode()

	@Override
	override fun toString(): String {
		return this::class.simpleName + "(id = $id )"
	}
}