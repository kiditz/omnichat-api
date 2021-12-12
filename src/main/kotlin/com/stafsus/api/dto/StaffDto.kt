package com.stafsus.api.dto

import com.stafsus.api.entity.*
import com.stafsus.api.validation.ValidAuthority
import org.apache.commons.lang3.RandomStringUtils
import javax.validation.constraints.*

data class StaffDto(
	@field:NotBlank
	val firstName: String? = null,
	@field:NotNull
	val lastName: String? = null,

	@field:Email
	@field:NotBlank
	@field:Size(max = 100)
	val email: String? = null,
	@field:NotBlank
	@field:Size(min = 8)
	val password: String? = null,
	@field:ValidAuthority
	val authority: List<String>? = null,
) {
	fun toEntity(company: Company): Staff {
		return Staff(
			firstName = firstName!!,
			lastName = lastName!!,
			email = email!!,
			company = company,
			status = StaffStatus.ACTIVE,
			authority = authority!!.joinToString(","),
			invitationCode = RandomStringUtils.randomAlphabetic(10)
		)
	}
}
