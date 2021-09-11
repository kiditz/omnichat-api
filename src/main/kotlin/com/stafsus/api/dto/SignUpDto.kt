package com.stafsus.api.dto

import com.stafsus.api.entity.Authority
import com.stafsus.api.entity.Status
import com.stafsus.api.entity.UserPrincipal
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class SignUpDto(
	@field:Email
	@field:NotBlank
	@field:Size(max = 100)
	val email: String? = null,
	@field:NotBlank
	val businessName: String? = null,
	@field:NotBlank
	@field:Size(min = 8)
	val password: String? = null,
) {
	fun toUserAdmin(): UserPrincipal {
		return UserPrincipal(
			email = email!!,
			password = password!!,
			businessName = businessName!!,
			authorities = setOf(Authority.ADMIN),
			status = Status.ACTIVE
		)
	}
}
