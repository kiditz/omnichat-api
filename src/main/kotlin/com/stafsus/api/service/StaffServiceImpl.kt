package com.stafsus.api.service

import com.stafsus.api.config.ThreadLocalStorage
import com.stafsus.api.constant.FtlTemplate
import com.stafsus.api.constant.MessageKey
import com.stafsus.api.dto.MailMessageDto
import com.stafsus.api.dto.StaffDto
import com.stafsus.api.entity.Staff
import com.stafsus.api.entity.StaffStatus
import com.stafsus.api.entity.UserCompany
import com.stafsus.api.entity.UserPrincipal
import com.stafsus.api.exception.AccessDeniedException
import com.stafsus.api.exception.ValidationException
import com.stafsus.api.repository.*
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StaffServiceImpl(
	private val staffRepository: StaffRepository,
	private val companyRepository: CompanyRepository,
	private val userRepository: UserRepository,
	private val userAuthorityRepository: UserAuthorityRepository,
	private val userCompanyRepository: UserCompanyRepository,
	@Value("\${app.invitationUrl}") val invitationUrl: String,
	private val rabbitService: RabbitService,
) : StaffService {

	@Transactional
	override fun addStaff(staffDto: StaffDto, userPrincipal: UserPrincipal): Staff {
		val companyId = ThreadLocalStorage.getTenantId()
			?: throw ValidationException(MessageKey.COMPANY_REQUIRED)
		val company = companyRepository.findById(companyId)
			.orElseThrow { ValidationException(MessageKey.COMPANY_NOT_FOUND) }
		val staff = staffRepository
			.findByEmailAndCompanyId(staffDto.email!!, companyId).orElse(staffDto.toEntity(company))
		staff.invitationCode = getUniqueInvitationCode()

		val map = mapOf(
			"email" to staff.email,
			"companyName" to company.name,
			"actionUrl" to "${invitationUrl}?code=${staff.invitationCode}"
		)
		val mailMessage = MailMessageDto(
			staffDto.email,
			"Invitation | StafSus",
			template = FtlTemplate.EMAIL_INVITATION,
			message = map,
			isHtml = true
		)
		rabbitService.sendEmail(mailMessage)
		return staffRepository.save(staff)
	}

	@Transactional
	override fun acceptStaff(invitationCode: String, user: UserPrincipal): Staff {
		val staff = staffRepository.findByInvitationCode(invitationCode)
			.orElseThrow { ValidationException(MessageKey.STAFF_NOT_FOUND) }
		val staffUser =
			userRepository.findByEmail(staff.email).orElseThrow { ValidationException(MessageKey.USER_NOT_FOUND) }
		if (staffUser.email != user.email) {
			throw AccessDeniedException(MessageKey.INVALID_USER_TO_VERIFY_STAFF, staff.email)
		}
		val authorities = staff.authority.split(",")
		authorities.forEach {
			val authority = userAuthorityRepository.findByAuthority(it).orElse(null)
			if (authority != null) {
				val userCompany = UserCompany(
					userAuthority = authority,
					company = staff.company,
					userPrincipal = staffUser
				)
				userCompanyRepository.save(userCompany)
			}
		}
		staff.status = StaffStatus.ACTIVE
		return staffRepository.save(staff)
	}

	@Transactional
	override fun declineStaff(invitationCode: String): Staff {
		val staff = staffRepository.findByInvitationCode(invitationCode)
			.orElseThrow { ValidationException(MessageKey.STAFF_NOT_FOUND) }
		staff.status = StaffStatus.DECLINED
		return staffRepository.save(staff)
	}

	@Transactional(readOnly = true)
	override fun checkStaffAccount(invitationCode: String): Boolean {
		val staff = staffRepository.findByInvitationCode(invitationCode)
			.orElseThrow { ValidationException(MessageKey.STAFF_NOT_FOUND) }
		return userRepository.existsByEmail(staff.email)
	}

	private fun getUniqueInvitationCode(): String {
		var invitationCode: String
		while (true) {
			invitationCode = RandomStringUtils.randomAlphanumeric(10)
			if (!staffRepository.existsByInvitationCode(invitationCode)) {
				break
			}
		}
		return invitationCode
	}
}