package com.stafsus.waapi.api.model

import javax.validation.constraints.NotBlank

data class RefreshTokenRequest(
    @NotBlank
    val refreshToken: String? = null
)
