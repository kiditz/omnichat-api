package com.stafsus.api.service

import com.stafsus.api.dto.AccessTokenDto
import com.stafsus.api.dto.UserDetailDto
import com.stafsus.api.entity.UserPrincipal
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtServiceImpl(
	@Value("\${security.jwtSecret}") val jwtSecret: String,
	@Value("\${security.jwtExpirationInMs}") val jwtExpirationInMs: Long,
	@Value("\${security.refreshExpirationDateInMs}") val refreshExpirationDateInMs: Long
) : JwtService {
	override fun generate(userPrincipal: UserPrincipal): AccessTokenDto {
		val now = Date()
		val expiryDate = Date(now.time + jwtExpirationInMs)
		val roles = userPrincipal.authorities
		val claims = mutableMapOf<String, Any>()
		claims["roles"] = roles
		val token = Jwts.builder()
			.setClaims(claims)
			.setIssuer(userPrincipal.email)
			.setSubject(userPrincipal.id.toString())
			.setIssuedAt(Date(System.currentTimeMillis()))
			.setExpiration(expiryDate)
			.signWith(SignatureAlgorithm.HS512, jwtSecret)
			.compact()
		return AccessTokenDto(accessToken = token, expiryDate = expiryDate.time)
	}

	override fun generate(authentication: Authentication): AccessTokenDto {
		val userPrincipal = authentication.principal as UserDetailDto
		return generate(userPrincipal.user)
	}

	override fun generateRefreshToken(id: Long): String {
		return Jwts.builder().setSubject(id.toString())
			.setIssuedAt(Date(System.currentTimeMillis()))
			.setExpiration(Date(System.currentTimeMillis() + refreshExpirationDateInMs))
			.signWith(SignatureAlgorithm.HS512, jwtSecret).compact()
	}

	/**
	 * @return null if token expired
	 * */
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