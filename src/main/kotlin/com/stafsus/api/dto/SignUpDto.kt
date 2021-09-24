package com.stafsus.api.dto

import com.stafsus.api.entity.Status
import com.stafsus.api.entity.UserPrincipal
import javax.validation.constraints.*

data class SignUpDto(
	@field:Email
	@field:NotBlank
	@field:Size(max = 100)
	val email: String? = null,
	@field:NotBlank
	val companyName: String? = null,
	@field:NotNull
	@field:Positive
	val industryId: Long? = null,
	@field:NotBlank
	@field:Size(min = 8)
	val password: String? = null,
	) {
	fun toUserAdmin(): UserPrincipal {
		return UserPrincipal(
			email = email!!,
			password = password!!,
			status = Status.ACTIVE,
			isVerified = false
		)
	}
}
