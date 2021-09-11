package com.stafsus.api.entity

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table
data class Quota(
	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
	@Column(nullable = false)
	val maxAgent: Int,
	@Column(nullable = false)
	val maxChannel: Int,
	val monthlyActiveVisitor: Int? = null,
	val expiredAt: LocalDateTime? = null
) : Auditable()