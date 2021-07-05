package com.stafsus.waapi.service.security

import com.stafsus.waapi.constant.MessageKey
import com.stafsus.waapi.entity.BlockAccessToken
import com.stafsus.waapi.entity.RefreshToken
import com.stafsus.waapi.entity.Status
import com.stafsus.waapi.entity.User
import com.stafsus.waapi.exception.ValidationException
import com.stafsus.waapi.repository.BlockAccessTokenRepository
import com.stafsus.waapi.repository.RefreshTokenRepository
import com.stafsus.waapi.repository.UserRepository
import com.stafsus.waapi.service.dto.*
import com.stafsus.waapi.utils.Common
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

@Service
class AuthenticationServiceImpl(
	private val userRepository: UserRepository,
	private val authenticationManager: AuthenticationManager,
	private val jwtProvider: JwtProvider,
	private val refreshTokenRepository: RefreshTokenRepository,
	private val passwordEncoder: PasswordEncoder,
	private val blockTokenRepository: BlockAccessTokenRepository,

	) : AuthenticationService {

	@Transactional(readOnly = true)
	override fun findUser(email: String): UserDto {
		val user = userRepository.findByEmail(email).orElseThrow { ValidationException(MessageKey.USER_NOT_FOUND) }
		return UserDto.fromUser(user)
	}

	@Transactional
	override fun signIn(signInDto: SignInDto): TokenDto {
		val authenticationToken = UsernamePasswordAuthenticationToken(signInDto.email, signInDto.password)
		try {
			val authentication = authenticationManager.authenticate(authenticationToken)
			val accessToken = jwtProvider.generateToken(authentication)
			val userId = jwtProvider.getUserIdFromToken(accessToken.accessToken)!!
			val user = userRepository.findById(userId)
				.orElseThrow { ValidationException(MessageKey.USER_NOT_FOUND) }
			accessToken.refreshToken = jwtProvider.generateRefreshToken(userId)
			val refreshToken = RefreshToken(
				token = accessToken.refreshToken!!,
				user = user,
				expiryDate = LocalDateTime.ofInstant(
					Instant.ofEpochMilli(accessToken.expiryDate!!),
					TimeZone.getDefault().toZoneId()
				)
			)
			refreshTokenRepository.save(refreshToken)
			return accessToken
		} catch (ex: AuthenticationException) {
			throw ValidationException(MessageKey.INVALID_EMAIL_OR_PASSWORD)
		}

	}

	@Transactional
	override fun signUp(signUpDto: SignUpDto): UserDto {
		if (userRepository.existsByEmail(signUpDto.email)) {
			throw ValidationException(MessageKey.EMAIL_EXISTS)
		}
		val user = User(
			email = signUpDto.email,
			username = Common.getUsernameByEmail(signUpDto.email),
			password = passwordEncoder.encode(signUpDto.password),
			authorities = signUpDto.authorities,
			role = signUpDto.role,
			status = Status.ACTIVE
		)
		userRepository.save(user)
//        waDeviceService.install(user)
		return UserDto.fromUser(user)
	}

	@Transactional
	override fun refreshToken(token: String): TokenDto {

		var refreshToken = refreshTokenRepository.findByToken(token)
			.orElseThrow { ValidationException(MessageKey.INVALID_REFRESH_TOKEN) }
		if (!jwtProvider.validateToken(token)) {
			refreshTokenRepository.delete(refreshToken)
			throw ValidationException(MessageKey.INVALID_REFRESH_TOKEN)
		}
		val user = userRepository.findById(refreshToken?.user?.id!!)
			.orElseThrow { ValidationException(MessageKey.USER_NOT_FOUND) }
		val principal = UserPrincipal(user = user)
		val newRefreshToken = jwtProvider.generateRefreshToken(user.id!!)
		val tokenDto = jwtProvider.generateToken(principal).copy(refreshToken = newRefreshToken)
		val jwtExpired =
			LocalDateTime.ofInstant(Instant.ofEpochMilli(tokenDto.expiryDate!!), TimeZone.getDefault().toZoneId())
		refreshToken = refreshToken.copy(token = newRefreshToken, expiryDate = jwtExpired)
		refreshTokenRepository.save(refreshToken)
		return tokenDto
	}

	@Transactional
	override fun signOut(refreshToken: String, accessToken: String) {
		refreshTokenRepository.findByToken(refreshToken)
			.orElseThrow { ValidationException(MessageKey.INVALID_REFRESH_TOKEN) }
		refreshTokenRepository.deleteByToken(refreshToken)
		val blockToken = BlockAccessToken(token = accessToken)
		blockTokenRepository.save(blockToken)
	}
}