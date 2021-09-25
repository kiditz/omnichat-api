package com.stafsus.api.entity

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity
@Table
data class Contact(
	@Id
	@Column(nullable = false)
	val id: String? = null,
	var number: String? = null,
	var server: String? = null,
	var name: String? = null,
	var picture: String? = null,
	var about: String? = null,
	@Enumerated(EnumType.STRING)
	var source: ContactSource,
	var isGroup: Boolean? = null,
	@OneToOne
	@JoinColumn(name = "company_id")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	var company: Company? = null,
) : Auditable()
