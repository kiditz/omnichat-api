package com.stafsus.api.entity

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.stafsus.api.utils.UnixToInstantConverter
import java.time.OffsetDateTime
import javax.persistence.*

@Entity
@Table
data class Message(
	@Id
	@Column(name = "id", nullable = false)
	var id: String? = null,
	@field:JsonProperty("deviceType")
	var deviceType: String? = null,
	@field:JsonProperty("server")
	val server: String? = null,
	@field:JsonProperty("isStatus")
	val isStatus: Boolean? = null,
	@field:JsonProperty("ack")
	val ack: Int? = null,
	@field:JsonProperty("isStarred")
	val isStarred: Boolean? = null,
	@field:JsonProperty("fromMe")
	val fromMe: Boolean? = null,
	@field:JsonProperty("hasMedia")
	val hasMedia: Boolean? = null,
	@field:JsonProperty("type")
	val type: String? = null,
	@field:JsonProperty("forwardingScore")
	val forwardingScore: Int? = null,
	@field:JsonProperty("vCards")
	val vCards: String? = null,
	@Column(name = "from_user")
	@field:JsonProperty("from")
	val from: String? = null,
	@Column(name = "has_quoted_msg")
	@field:JsonProperty("hasQuotedMsg")
	val hasQuotedMsg: Boolean? = null,
	@field:JsonProperty("location")
	val location: String? = null,
	@field:JsonProperty("links")
	val links: String? = null,
//	@field:JsonProperty("userSerialized")
//	val userSerialized: String? = null,
	@Column(name = "to_user")
	@field:JsonProperty("to")
	val to: String? = null,
	@field:JsonProperty("number")
	val number: String? = null,
	@field:JsonProperty("broadcast")
	val broadcast: Boolean? = null,
	@field:JsonProperty("isForwarded")
	val isForwarded: Boolean? = null,
	@field:JsonProperty("serialized")
	val serialized: String? = null,
	var body: String? = null,
	@field:JsonProperty("timestamp")
	@JsonDeserialize(converter = UnixToInstantConverter::class)
	val timestamp: OffsetDateTime? = null,
	@field:JsonProperty("mentionedIds")
	val mentionedIds: String? = null,
	@ManyToOne(cascade = [CascadeType.ALL])
	@JoinColumn(name = "user_serialized")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	var chat: Chat? = null,
) : Auditable()
