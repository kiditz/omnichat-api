package com.stafsus.api.entity

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonProperty
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
	var email: String,
	var authority: String,
	@Enumerated(EnumType.STRING)
	var status: StaffStatus,
	@OneToOne
	@JoinColumn(name = "company_id")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	var company: Company? = null,
	@ManyToMany(cascade = [CascadeType.ALL])
	@JoinTable(
		name = "staff_assignment",
		joinColumns = [JoinColumn(name = "staff_id")],
		inverseJoinColumns = [JoinColumn(name = "product_id")]
	)
	var products: List<Product>? = null
) : Auditable()
