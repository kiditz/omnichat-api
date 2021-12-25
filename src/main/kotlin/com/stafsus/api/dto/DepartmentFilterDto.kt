package com.stafsus.api.dto

data class DepartmentFilterDto(
	val name: String?,
	val page: Int,
	val size: Int
)