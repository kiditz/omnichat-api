package com.stafsus.api.entity

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.stafsus.api.utils.UnixToInstantConverter
import java.time.Instant
import java.time.OffsetDateTime
import javax.persistence.*

@Entity
@Table(name = "chat")
data class Chat(
	@Id
	@Column(name = "id", nullable = false)
	val id: String? = null,
	val number: String? = null,
	val name: String? = null,
	val server: String? = null,
	val archived: Boolean? = null,
	@JsonProperty("isGroup")
	@Column(name = "is_group", nullable = false)
	val group: Boolean? = null,
	@JsonProperty("isReadOnly")
	@Column(name = "is_read_only", nullable = false)
	val readOnly: Boolean? = null,
	@JsonProperty("pinned")
	@Column(name = "pinned", nullable = false)
	val pinned: Boolean? = null,
	@JsonProperty("isMuted")
	@Column(name = "is_muted", nullable = false)
	val muted: Boolean? = null,
	val unreadCount: Long? = null,
	@JsonDeserialize(converter = UnixToInstantConverter::class)
	val muteExpiration: OffsetDateTime? = null,
	@JsonDeserialize(converter = UnixToInstantConverter::class)
	val timestamp: OffsetDateTime? = null,
	@JsonProperty("groupMetadata")
	@OneToOne(cascade = [CascadeType.ALL])
	@JoinColumn(name = "group_id")
	val groupMetadata: GroupMetadata? = null,
	@OneToOne
	@JoinColumn(name = "user_id")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	var user: UserPrincipal? = null,
) : Auditable()