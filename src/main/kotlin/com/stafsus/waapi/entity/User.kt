package com.stafsus.waapi.entity

import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "user_principal")
data class User(
	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
	@Column(nullable = false)
	var email: String,
	@Column(nullable = false)
	val username: String,
	@Column(nullable = false)
	var password: String,

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	var role: Role,

	@ElementCollection(fetch = FetchType.EAGER, targetClass = Authority::class)
	@CollectionTable(name = "user_authority", joinColumns = [JoinColumn(name = "user_id")])
	@Column(name = "authority")
	var authorities: Set<Authority>,

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	var status: Status,

	var startTrialAt: LocalDateTime? = null,
	var endTrialAt: LocalDateTime? = null
) : Auditable()
