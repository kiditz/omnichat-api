package com.stafsus.api.service

import com.stafsus.api.constant.MessageKey
import com.stafsus.api.dto.AccessTokenDto
import com.stafsus.api.dto.UserDetailDto
import com.stafsus.api.entity.RefreshToken
import com.stafsus.api.exception.ValidationException
import com.stafsus.api.repository.RefreshTokenRepository
import com.stafsus.api.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.LocalDateTime
import java.util.*


@Service
class AuthenticationServiceImpl(
	private val authenticationManager: AuthenticationManager,
	private val jwtService: JwtService,
	private val refreshTokenRepository: RefreshTokenRepository,
	private val userRepository: UserRepository,

	@Value("\${security.refreshExpirationDateInMs}") val refreshExpirationDateInMs: Long

) : AuthenticationService {
	private val log = LoggerFactory.getLogger(javaClass)

	@Transactional
	override fun signIn(email: String, password: String): AccessTokenDto {
		try {
			val authenticationToken = UsernamePasswordAuthenticationToken(email, password)
			val authentication = authenticationManager.authenticate(authenticationToken)
			val userDetailDto = authentication.principal as UserDetailDto

			val accessToken = jwtService.generate(authentication)
			accessToken.refreshToken = jwtService.generateRefreshToken(userDetailDto.user.id!!)

			val refreshToken = RefreshToken(
				token = accessToken.refreshToken!!,
				user = userDetailDto.user,
				expiryDate = LocalDateTime.ofInstant(
					Instant.ofEpochMilli(Date(System.currentTimeMillis() + refreshExpirationDateInMs).time),
					TimeZone.getDefault().toZoneId()
				)
			)
			refreshTokenRepository.save(refreshToken)
			return accessToken
		} catch (e: Exception) {
			log.info("Exception", e)
			throw ValidationException(MessageKey.INVALID_EMAIL_OR_PASSWORD)
		}
	}

	override fun refresh(token: String): AccessTokenDto {
		if (!jwtService.validateToken(token)) {
			refreshTokenRepository.deleteByToken(token)
			throw ValidationException(MessageKey.INVALID_REFRESH_TOKEN)
		}
		val refreshToken = refreshTokenRepository.findByToken(token)
			.orElseThrow { ValidationException(MessageKey.INVALID_REFRESH_TOKEN) }
		val user = userRepository.findById(refreshToken?.user?.id!!)
			.orElseThrow { ValidationException(MessageKey.USER_NOT_FOUND) }
		val newRefreshToken = jwtService.generateRefreshToken(user.id!!)
		val expireRefreshToken = LocalDateTime.ofInstant(
			Instant.ofEpochMilli(Date(System.currentTimeMillis() + refreshExpirationDateInMs).time),
			TimeZone.getDefault().toZoneId()
		)
		refreshToken.token = newRefreshToken
		refreshToken.expiryDate = expireRefreshToken
		refreshTokenRepository.save(refreshToken)
		return jwtService.generate(user).copy(refreshToken = newRefreshToken)
	}

}