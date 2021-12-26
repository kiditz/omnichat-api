package com.stafsus.api.entity

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.stafsus.api.utils.UnixToInstantConverter
import org.hibernate.Hibernate
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
	val source: String? = null,
	val archived: Boolean? = null,
	@field:JsonProperty("isGroup")
	@Column(name = "is_group", nullable = false)
	val group: Boolean? = null,
	@field:JsonProperty("isReadOnly")
	@Column(name = "is_read_only", nullable = false)
	val readOnly: Boolean? = null,
	@field:JsonProperty("pinned")
	@Column(name = "pinned", nullable = false)
	val pinned: Boolean? = null,
	@field:JsonProperty("isMuted")
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
	@JoinColumn(name = "id")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	var contact: Contact? = null,
	@OneToOne
	@JoinColumn(name = "company_id")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	var company: Company? = null,
	@OneToMany(mappedBy = "chat", cascade = [CascadeType.ALL])
	var messages: List<Message>? = null
) : Auditable() {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
		other as Chat

		return id != null && id == other.id
	}

	override fun hashCode(): Int = javaClass.hashCode()

	@Override
	override fun toString(): String {
		return this::class.simpleName + "(id = $id )"
	}
}