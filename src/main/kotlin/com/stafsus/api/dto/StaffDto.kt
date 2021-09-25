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
	@field:NotNull
	@field:NotBlank
	val phone: String? = null,
	@field:Email
	@field:NotBlank
	@field:Size(max = 100)
	val email: String? = null,
	@field:ValidAuthority
	val authority: List<String>? = null,
) {
	fun toEntity(company: Company): Staff {
		return Staff(
			firstName = firstName!!,
			lastName = lastName!!,
			email = email!!,
			phone = phone!!,
			company = company,
			status = StaffStatus.INVITED,
			authority = authority!!.joinToString(","),
			invitationCode = RandomStringUtils.randomAlphabetic(10)
		)
	}
}
