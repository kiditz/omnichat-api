package com.stafsus.waapi.api.model

import com.stafsus.waapi.service.dto.SignInDto
import javax.validation.constraints.Email
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class SignInRequest(
    @field:Email
    @field:NotBlank
    @field:Size(max = 100)
    val email: String,
    @field:NotBlank
    @field:Size(min = 8, max = 60)
    val password: String,
) {
    fun toDto(): SignInDto {
        return SignInDto(email = email, password = password)
    }
}