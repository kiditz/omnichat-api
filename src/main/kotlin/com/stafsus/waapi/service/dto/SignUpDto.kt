package com.stafsus.waapi.service.dto

import com.stafsus.waapi.entity.Authority
import com.stafsus.waapi.entity.Role

data class SignUpDto(
    val email: String,
    val password: String,
    val role: Role,
    val authorities: Set<Authority>
)