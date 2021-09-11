package com.stafsus.api.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.AuditorAware
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.Authentication
import java.util.*
import org.springframework.security.core.context.SecurityContextHolder


class AuditAwareImpl : AuditorAware<String> {
	private val log: Logger = LoggerFactory.getLogger(AuditAwareImpl::class.java)
	override fun getCurrentAuditor(): Optional<String> {
		val authentication: Authentication? = SecurityContextHolder.getContext().authentication
		if (authentication == null ||
			!authentication.isAuthenticated ||
			authentication is AnonymousAuthenticationToken
		) {
			log.debug("AnonymousAuthenticationToken")
			return Optional.empty()
		}
		log.debug("Token Exists")
		return Optional.ofNullable(authentication.name)
	}
}