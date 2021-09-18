package com.stafsus.api.dto

import com.stafsus.api.entity.Authority
import com.stafsus.api.entity.Staff
import com.stafsus.api.entity.UserPrincipal
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class StaffDto(
	@field:NotNull
	@field:NotBlank
	val firstName: String? = null,
	@field:NotNull
	@field:NotBlank
	val lastName: String? = null,
	@field:NotNull
	@field:NotBlank
	val phone: String? = null,
	@field:NotNull
	@field:Email
	val email: String? = null,
	@field:NotNull
	val authority: Authority? = null
) {
	fun toEntity(userPrincipal: UserPrincipal): Staff {
		return Staff(
			firstName = firstName!!,
			lastName = lastName!!,
			phone = phone!!,
			user = userPrincipal
		)
	}
}
