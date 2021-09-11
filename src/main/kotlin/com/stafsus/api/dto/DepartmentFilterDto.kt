package com.stafsus.api.dto

import com.stafsus.api.entity.UserPrincipal

data class DepartmentFilterDto(
	val userPrincipal: UserPrincipal,
	val name: String?,
	val page: Int,
	val size: Int
)