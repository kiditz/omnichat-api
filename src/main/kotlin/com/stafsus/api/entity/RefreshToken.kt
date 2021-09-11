package com.stafsus.api.entity

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "refresh_token")
data class RefreshToken(
	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
	@Column(nullable = false, length = 60)
	var token: String,
	@Column(name = "expiry_date")
	var expiryDate: LocalDateTime,
	@OneToOne
	@JoinColumn(name = "user_id")
	var user: UserPrincipal,
) : Auditable()
