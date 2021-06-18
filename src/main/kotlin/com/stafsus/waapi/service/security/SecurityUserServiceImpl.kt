package com.stafsus.waapi.service.security

import com.stafsus.waapi.constant.MessageKey
import com.stafsus.waapi.entity.User
import com.stafsus.waapi.exception.ValidationException
import com.stafsus.waapi.repository.BlockAccessTokenRepository
import com.stafsus.waapi.repository.UserRepository
import com.stafsus.waapi.service.dto.UserPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

@Service
class SecurityUserServiceImpl(
    private val userRepository: UserRepository,
    private val blockTokenRepository: BlockAccessTokenRepository
) : SecurityUserService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user =
            userRepository.findByEmail(email = username).orElseThrow { ValidationException(MessageKey.USER_NOT_FOUND) }
        return UserPrincipal(user = user)

    }

    override fun loadUserId(id: Long): Optional<UserPrincipal> {
        val user: Optional<User> = userRepository.findById(id)
        return user.map { UserPrincipal(user = it) }
    }

    override fun isBlocked(accessToken: String?): Boolean {
        return blockTokenRepository.existsByToken(accessToken)
    }
}