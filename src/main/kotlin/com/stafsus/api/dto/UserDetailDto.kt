package com.stafsus.api.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.stafsus.api.entity.Authority
import com.stafsus.api.entity.Status
import com.stafsus.api.entity.UserPrincipal
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class UserDetailDto(
	val user: UserPrincipal,
	val authorities: Set<Authority>
) : UserDetails {
	override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
		val authList = mutableListOf<SimpleGrantedAuthority>()
		authList.addAll(authorities.map { SimpleGrantedAuthority(it.name) })
		return authList
	}

	@JsonIgnore
	override fun getPassword(): String {
		return user.password
	}

	override fun getUsername(): String {
		return user.email
	}

	override fun isAccountNonExpired(): Boolean {
		return true
	}

	override fun isAccountNonLocked(): Boolean {
		return true
	}

	override fun isCredentialsNonExpired(): Boolean {
		return true
	}

	override fun isEnabled(): Boolean {
		return user.status == Status.ACTIVE
	}
}