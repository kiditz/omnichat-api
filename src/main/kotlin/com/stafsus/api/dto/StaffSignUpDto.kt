package com.stafsus.api.dto

import com.stafsus.api.entity.Status
import com.stafsus.api.entity.UserPrincipal
import javax.validation.constraints.*

data class StaffSignUpDto(
	@field:NotBlank
	@field:Size(min = 8)
	val password: String? = null,
	@field:NotNull
	@field:NotBlank
	val invitationCode: String? = null,
) {
	fun toUser(email: String): UserPrincipal {
		return UserPrincipal(
			email = email,
			password = password!!,
			status = Status.ACTIVE,
			isVerified = false
		)
	}
}
