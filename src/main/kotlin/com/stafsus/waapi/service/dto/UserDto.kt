package com.stafsus.waapi.service.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.stafsus.waapi.entity.*


data class UserDto(
    val id: Long,
    var email: String,
    @JsonIgnore
    var password: String,
    var role: Role,
    var authorities: Set<Authority>,
    var status: Status

) {
    companion object {
        fun fromUser(user: User): UserDto {
            return UserDto(
                id = user.id!!,
                email = user.email,
                password = user.password,
                role = user.role,
                authorities = user.authorities,
                status = user.status,
            )
        }
    }


}
