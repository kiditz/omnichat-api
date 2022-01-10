package com.stafsus.api.entity

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass
import javax.persistence.Version

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class Auditable {
	@CreatedBy
	@Column(updatable = false)
	var createdBy: String? = null

	@CreatedDate
	@Column(nullable = false, updatable = false)
	var createdAt: Instant = Instant.now()

	@LastModifiedBy
	var updatedBy: String? = null

	@LastModifiedDate
	var updatedAt: Instant? = null

	@Version
	var version: Long = 0
}