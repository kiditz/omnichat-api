package com.stafsus.waapi.service.dto

import com.stafsus.waapi.entity.Status
import com.stafsus.waapi.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.security.Principal

data class UserPrincipal(
    val user: User
) : UserDetails, Principal {
    override fun getAuthorities(): List<GrantedAuthority> {
        val authorities = mutableListOf<SimpleGrantedAuthority>()
        authorities.add(SimpleGrantedAuthority(user.role.name))
        authorities.addAll(user.authorities.map { SimpleGrantedAuthority(it.name) }.toList())
        return authorities
    }

    override fun getPassword(): String {
        return user.password
    }

    override fun getUsername(): String {
        return user.email
    }

    override fun isAccountNonExpired(): Boolean {
        return user.status != Status.EXPIRED
    }

    override fun isAccountNonLocked(): Boolean {
        return user.status != Status.BLOCKED
    }

    override fun isCredentialsNonExpired(): Boolean {
        return user.status != Status.CREDENTIAL_EXPIRED
    }

    override fun isEnabled(): Boolean {
        return user.status == Status.ACTIVE
    }

    override fun getName(): String {
        return user.email
    }
}
