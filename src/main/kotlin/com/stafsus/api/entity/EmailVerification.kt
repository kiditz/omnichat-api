package com.stafsus.api.entity

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table
data class EmailVerification(
	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
	var email: String,
	var verificationCode: String,
	var expireAt: LocalDateTime,
	@OneToOne
	@JoinColumn(name = "user_id")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	var user: UserPrincipal? = null,
) : Auditable()
