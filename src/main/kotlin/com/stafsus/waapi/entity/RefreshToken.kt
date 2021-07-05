package com.stafsus.waapi.entity

import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class RefreshToken(
	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
	@Column(nullable = false)
	val token: String,
	val expiryDate: LocalDateTime? = null,
	@OneToOne
	@JoinColumn(name = "user_id")
	var user: User? = null,
) : Auditable()