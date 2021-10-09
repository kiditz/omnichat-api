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
	var user: UserPrincipal? = null,
	@OneToOne
	@JoinColumn(name = "industry_id")
//	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	var industry: Industry? = null,
) : Auditable()
