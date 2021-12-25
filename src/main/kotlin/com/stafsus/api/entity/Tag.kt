package com.stafsus.api.entity

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity
@Table(name = "tag")
data class Tag(
	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
	@Column(nullable = false, length = 60)
	var name: String,
	@Column(nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	var type: TagType,
	@Column(nullable = false, length = 10)
	var color: String,
	@OneToOne
	@JoinColumn(name = "user_id")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	var company: Company? = null,
) : Auditable()