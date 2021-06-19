package com.stafsus.waapi.api.model

import com.stafsus.waapi.entity.Authority
import com.stafsus.waapi.entity.Role
import com.stafsus.waapi.service.dto.SignUpDto
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class SignUpRequest(
        @field:Email
        @field:NotBlank
        @field:Size(max = 100)
        val email: String,
        @field:NotBlank
        @field:Size(max = 60)
        val password: String,
        @NotNull
        val role: Role,
) {
    fun toDto(): SignUpDto {
        return SignUpDto(
                email = email,
                password = password,
                role = role,
                authorities = mutableSetOf(Authority.API_ACCESS, Authority.EDIT_WEBHOOK, Authority.VIEW_WEBHOOK)
        )
    }
}