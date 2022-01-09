package com.stafsus.api.projection

import org.springframework.beans.factory.annotation.Value

interface StaffView {
	@Value("#{target.id}")
	fun getId(): Long

	@Value("#{target.user.name}")
	fun getName(): String

	@Value("#{target.authority.authority}")
	fun getAuthority(): String

	@Value("#{target.user.email}")
	fun getEmail(): String

	@Value("#{target.user.imageUrl}")
	fun getImage(): String

	fun getChannels(): HashSet<StaffChannelView>
}
