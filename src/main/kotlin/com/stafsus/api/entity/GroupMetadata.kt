package com.stafsus.api.entity

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.stafsus.api.utils.UnixToInstantConverter
import java.time.Instant
import java.time.OffsetDateTime
import javax.persistence.*

@Entity
@Table(name = "group_metadata")
data class GroupMetadata(
	@Id
	@Column(name = "id", nullable = false)
	val id: String? = null,
	@JsonDeserialize(converter = UnixToInstantConverter::class)
	val creation: OffsetDateTime? = null,
	val owner: String? = null,
	val ownerId: String? = null,
	val server: String? = null,
	@OneToMany(mappedBy = "groupMetadata", cascade = [CascadeType.ALL])
	var participants: List<Participant>? = null
) : Auditable()