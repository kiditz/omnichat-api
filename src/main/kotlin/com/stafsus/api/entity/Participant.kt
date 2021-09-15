package com.stafsus.api.entity

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity
@Table
class Participant(
	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
	var number: String? = null,
	var server: String? = null,
	@Column(name = "is_admin")
	var admin: Boolean? = null,
	@ManyToOne
	@JoinColumn(name = "group_id")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	var groupMetadata: GroupMetadata? = null,
) : Auditable()