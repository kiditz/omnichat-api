package com.stafsus.api.projection

import org.springframework.beans.factory.annotation.Value

interface ChatProjection {
	fun getId(): String
	fun getNumber(): String
	fun getName(): String
	fun getServer(): String
	fun getSource(): String
	fun isArchived(): Boolean
	fun isGroup(): Boolean
	fun isReadOnly(): Boolean
	fun isPinned(): Boolean
	fun isMuted(): Boolean
	@Value("#{target.contact.picture}")
	fun getPicture(): String
}