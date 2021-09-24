package com.stafsus.api.entity

import javax.persistence.*

@Entity
@Table
data class UserAuthority(
	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
	@Column(nullable = false, length = 60)
	var authority: String,
)