package com.stafsus.waapi.service.security

import com.stafsus.waapi.constant.MessageKey
import com.stafsus.waapi.entity.*
import com.stafsus.waapi.exception.ValidationException
import com.stafsus.waapi.repository.BlockAccessTokenRepository
import com.stafsus.waapi.repository.RefreshTokenRepository
import com.stafsus.waapi.repository.UserRepository
import com.stafsus.waapi.service.WaService
import com.stafsus.waapi.service.dto.*
import com.stafsus.waapi.utils.Common
import com.stafsus.waapi.utils.Random
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class AuthenticationServiceImpl(
        private val userRepository: UserRepository,
        private val authenticationManager: AuthenticationManager,
        private val jwtProvider: JwtProvider,
        private val refreshTokenRepository: RefreshTokenRepository,
        private val passwordEncoder: PasswordEncoder,
        private val blockTokenRepository: BlockAccessTokenRepository,
        private val waService: WaService,
        @Value("\${app.device.period}") val period: Long,
) : AuthenticationService {
    @Transactional
    override fun signIn(signInDto: SignInDto): TokenDto {
        val authenticationToken = UsernamePasswordAuthenticationToken(signInDto.email, signInDto.password)
        try {
            val authentication = authenticationManager.authenticate(authenticationToken)
            val accessToken = jwtProvider.generateToken(authentication)
            val customerId = jwtProvider.getUserIdFromToken(accessToken.accessToken)!!
            accessToken.refreshToken = jwtProvider.generateRefreshToken(customerId)
            val refreshToken = RefreshToken(
                    token = accessToken.refreshToken!!,
                    user = userRepository.findById(customerId)
                            .orElseThrow { ValidationException(MessageKey.USER_NOT_FOUND) }
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
        val startAt = LocalDateTime.now()
        val endAt = LocalDateTime.now().plusDays(period)
        val device = WaDevice(deviceId = Random.stringLowerOnly(8), startAt = startAt, endAt = endAt, deviceStatus = DeviceStatus.ON_PROGRESS)
        user.devices = mutableSetOf(device)
        userRepository.save(user)
        waService.sendDeviceToQueue(user, device.deviceId)
        return UserDto.fromUser(user)
    }

    @Transactional
    override fun refreshToken(token: String): TokenDto {

        val refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow { ValidationException(MessageKey.INVALID_REFRESH_TOKEN) }
        if (!jwtProvider.validateToken(token)) {
            refreshTokenRepository.delete(refreshToken)
            throw ValidationException(MessageKey.INVALID_REFRESH_TOKEN)
        }
        val user = userRepository.findById(refreshToken?.user?.id!!)
                .orElseThrow { ValidationException(MessageKey.USER_NOT_FOUND) }
        val principal = UserPrincipal(user = user)
        val newRefreshToken = jwtProvider.generateRefreshToken(user.id!!)
        return jwtProvider.generateToken(principal).copy(refreshToken = newRefreshToken)
    }

    @Transactional
    override fun signOut(refreshToken: String, accessToken: String) {
        val refreshTokenData = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow { ValidationException(MessageKey.INVALID_REFRESH_TOKEN) }
        refreshTokenRepository.delete(refreshTokenData)
        val blockToken = BlockAccessToken(token = accessToken)
        blockTokenRepository.save(blockToken)
    }
}