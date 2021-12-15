package com.stafsus.api.dto

import com.stafsus.api.entity.Company
import com.stafsus.api.entity.ProductType
import com.stafsus.api.entity.Staff
import com.stafsus.api.entity.StaffStatus
import com.stafsus.api.validation.ValidAuthority
import com.stafsus.api.validation.ValidProductType
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

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
	@field:ValidProductType
	val products: List<String>? = null,
) {
	fun toEntity(company: Company): Staff {
		return Staff(
			firstName = firstName!!,
			lastName = lastName!!,
			email = email!!,
			company = company,
			status = StaffStatus.ACTIVE,
			authority = authority!!.joinToString(","),
		)
	}
}
