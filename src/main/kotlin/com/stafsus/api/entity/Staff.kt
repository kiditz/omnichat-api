package com.stafsus.api.entity

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
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
	@Column(name = "first_name")
	var firstName: String,
	@Column(name = "last_name")
	var lastName: String,
	var phone: String,
	var email: String,
	var invitationCode: String,
	var authority: String,
	@Enumerated(EnumType.STRING)
	var status: StaffStatus,
	@OneToOne
	@JoinColumn(name = "company_id")
//	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	var company: Company? = null,
) : Auditable()
