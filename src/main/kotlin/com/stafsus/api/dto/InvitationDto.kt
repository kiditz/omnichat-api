package com.stafsus.api.dto

import com.stafsus.api.entity.Authority
import com.stafsus.api.validation.ValueOfEnum
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

data class InvitationDto(
	@field:Email
	@field:NotBlank
	@field:Size(max = 100)
	val email: String? = null,
	@NotBlank
	@field:ValueOfEnum(enumClass = Authority::class)
	val authority: String? = null,
	@NotEmpty
	val channels: List<Long>? = null,
)