package com.stafsus.api.entity

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import org.hibernate.Hibernate
import javax.persistence.*

@Entity
@Table
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator::class,
	property = "id"
)
data class Staff(
	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
	@Enumerated(EnumType.STRING)
	var status: Status,
	@OneToOne
	@JoinColumn(name = "user_id")
	var user: UserPrincipal? = null,
	@OneToOne
	@JoinColumn(name = "company_id")
	var company: Company? = null,
	@OneToOne
	@JoinColumn(name = "authority_id")
	var authority: UserAuthority? = null,
) : Auditable() {
	@ManyToMany(cascade = [CascadeType.ALL])
	@JoinTable(
		name = "staff_assignment",
		joinColumns = [JoinColumn(name = "staff_id")],
		inverseJoinColumns = [JoinColumn(name = "channel_id")]
	)
	val channels: MutableSet<Channel> = HashSet()

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
		other as Staff

		return id != null && id == other.id
	}

	override fun hashCode(): Int = javaClass.hashCode()

	@Override
	override fun toString(): String {
		return this::class.simpleName + "(id = $id )"
	}

}
