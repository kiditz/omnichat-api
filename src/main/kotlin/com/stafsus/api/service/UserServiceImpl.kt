package com.stafsus.api.service

import com.stafsus.api.constant.MessageKey
import com.stafsus.api.dto.UserDetailDto
import com.stafsus.api.entity.Price
import com.stafsus.api.entity.Quota
import com.stafsus.api.entity.UserPrincipal
import com.stafsus.api.execption.ValidationException
import com.stafsus.api.repository.PriceRepository
import com.stafsus.api.repository.UserRepository
import org.springframework.http.MediaType
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class UserServiceImpl(
	private val userRepository: UserRepository,
	private val passwordEncoder: PasswordEncoder,
	private val priceRepository: PriceRepository,
	private val identIconService: IdentIconService,
	private val fileService: FileService
) : UserService {
	@Transactional
	override fun save(userPrincipal: UserPrincipal): UserPrincipal {
		if (userRepository.existsByEmail(userPrincipal.email)) {
			throw ValidationException(MessageKey.EMAIL_EXISTS)
		}
		userPrincipal.password = passwordEncoder.encode(userPrincipal.password)
		val icon = identIconService.saveBytes(identIconService.generateImage(userPrincipal.email, 400, 400))
		val destination = "profile"
		val image = fileService.saveOriginal(userPrincipal.email, destination, MediaType.IMAGE_PNG_VALUE, icon)
		userPrincipal.imageUrl = fileService.getImageUrl(image, destination)
		val price =
			priceRepository.findByName(Price.TRIAL).orElseThrow { ValidationException(MessageKey.PRICE_NOT_FOUND) }
		userPrincipal.quota = Quota(
			maxAgent = price.agentAmount!!.toInt(),
			maxChannel = price.channelAmount!!.toInt(),
			monthlyActiveVisitor = price.monthlyActiveVisitor,
			expiredAt = LocalDateTime.now().plusDays(price.accessTime)
		)
		return userRepository.save(userPrincipal)
	}

	override fun loadUserByUsername(username: String): UserDetails {
		val user = userRepository.findByEmail(username).orElseThrow { ValidationException(MessageKey.USER_NOT_FOUND) }
		return UserDetailDto(user)
	}

	override fun loadUserById(id: Long): UserDetails {
		val user = userRepository.findById(id).orElseThrow { ValidationException(MessageKey.USER_NOT_FOUND) }
		return UserDetailDto(user)
	}
}