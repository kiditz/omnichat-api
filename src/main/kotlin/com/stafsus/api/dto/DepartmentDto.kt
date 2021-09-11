package com.stafsus.api.dto

import com.stafsus.api.entity.Department
import com.stafsus.api.entity.Status
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class DepartmentDto(
	@field:NotBlank
	@field:Size(max = 60)
	val name: String? = null,
	@field:NotNull
	val status: Status? = null,
) {
	fun toEntity(): Department {
		return Department(
			name = name!!,
			status = status!!
		)
	}
}
