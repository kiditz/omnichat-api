package com.stafsus.waapi.service.security

import com.stafsus.waapi.service.dto.TokenDto
import com.stafsus.waapi.service.dto.UserPrincipal
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtProviderImpl(
	@Value("\${app.security.jwtSecret}") val jwtSecret: String,
	@Value("\${app.security.jwtExpirationInMs}") val jwtExpirationInMs: Long,
	@Value("\${app.security.refreshExpirationDateInMs}") val refreshExpirationDateInMs: Long
) : JwtProvider {
	override fun generateToken(authentication: Authentication): TokenDto {
		val userPrincipal = authentication.principal as UserPrincipal
		return generateToken(userPrincipal)
	}

	override fun generateToken(userPrincipal: UserPrincipal): TokenDto {
		val now = Date()
		val expiryDate = Date(now.time + jwtExpirationInMs)
		val roles: Collection<GrantedAuthority?> = userPrincipal.authorities
		val claims = mutableMapOf<String, Any>()
		claims["roles"] = roles
		val token = Jwts.builder()
			.setClaims(claims)
			.setIssuer(userPrincipal.username)
			.setSubject(userPrincipal.user.id.toString())
			.setIssuedAt(Date(System.currentTimeMillis()))
			.setExpiration(expiryDate)
			.signWith(SignatureAlgorithm.HS512, jwtSecret)
			.compact()
		return TokenDto(accessToken = token, expiryDate = expiryDate.time, refreshToken = "")
	}

	override fun generateRefreshToken(id: Long): String {
		return Jwts.builder().setSubject(id.toString())
			.setIssuedAt(Date(System.currentTimeMillis()))
			.setExpiration(Date(System.currentTimeMillis() + refreshExpirationDateInMs))
			.signWith(SignatureAlgorithm.HS512, jwtSecret).compact()
	}

	override fun getUserIdFromToken(token: String?): Long? {
		return try {
			val claims = Jwts.parser()
				.setSigningKey(jwtSecret)
				.parseClaimsJws(token)
				.body
			claims.subject.toLong()
		} catch (e: ExpiredJwtException) {
			null
		}
	}

	override fun validateToken(authToken: String?): Boolean {
		return try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken)
			true
		} catch (ex: Exception) {
			false
		}
	}
}