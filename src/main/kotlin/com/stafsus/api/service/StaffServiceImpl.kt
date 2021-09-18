package com.stafsus.api.service

import com.stafsus.api.dto.StaffDto
import com.stafsus.api.entity.EmailVerification
import com.stafsus.api.entity.Staff
import com.stafsus.api.entity.Status
import com.stafsus.api.entity.UserPrincipal
import com.stafsus.api.repository.EmailVerificationRepository
import com.stafsus.api.repository.StaffRepository
import com.stafsus.api.repository.UserRepository
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class StaffServiceImpl(
	private val staffRepository: StaffRepository,
	private val userRepository: UserRepository,
	private val emailVerificationRepository: EmailVerificationRepository
) : StaffService {

	@Transactional
	override fun addStaff(staffDto: StaffDto, userPrincipal: UserPrincipal): Staff {
		val user = UserPrincipal(
			email = staffDto.email!!,
			businessName = userPrincipal.businessName,
			authorities = setOf(staffDto.authority!!),
			status = Status.INACTIVE,
			parentId = userPrincipal.id,
			password = ""
		)
		val emailVerification = EmailVerification(
			email = staffDto.email,
			verificationToken = RandomStringUtils.randomAlphabetic(60),
			expireAt = LocalDateTime.now().plusDays(3)
		)
		emailVerificationRepository.save(emailVerification)
		userRepository.save(user)
		val staff = staffDto.toEntity(user)
		return staffRepository.save(staff)
	}
}