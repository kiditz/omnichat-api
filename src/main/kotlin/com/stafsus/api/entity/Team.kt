package com.stafsus.api.entity

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import org.hibernate.Hibernate
import java.time.Instant
import javax.persistence.*

@Entity
@Table
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator::class,
	property = "id"
)
data class Team(
	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long? = null,
	var picture: String? = null,
	var email: String? = null,
	@Enumerated(EnumType.STRING)
	var status: Status = Status.INACTIVE,
	var lastActivity: Instant? = null,
	@OneToOne
	@JoinColumn(name = "company_id")
	var company: Company? = null,


	) : Auditable() {

	@ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
	@JoinTable(
		name = "team_assignment",
		joinColumns = [JoinColumn(name = "team_id")],
		inverseJoinColumns = [JoinColumn(name = "channel_id")]
	)
	var channels: Set<Channel>? = null

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
		other as Team

		return id != null && id == other.id
	}

	override fun hashCode(): Int = javaClass.hashCode()

	@Override
	override fun toString(): String {
		return this::class.simpleName + "(id = $id )"
	}
}
