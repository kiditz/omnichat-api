package com.stafsus.api.service

import com.stafsus.api.config.ThreadLocalStorage
import com.stafsus.api.constant.FtlTemplate
import com.stafsus.api.constant.MessageKey
import com.stafsus.api.dto.MailMessageDto
import com.stafsus.api.dto.StaffDto
import com.stafsus.api.entity.Staff
import com.stafsus.api.entity.UserPrincipal
import com.stafsus.api.repository.CompanyRepository
import com.stafsus.api.repository.StaffRepository
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.xml.bind.ValidationException

@Service
class StaffServiceImpl(
	private val staffRepository: StaffRepository,
	private val companyRepository: CompanyRepository,
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
		val map = mapOf(
			"email" to staff.email,
			"companyName" to company.name,
			"actionUrl" to "${invitationUrl}?code=${staff.invitationCode}"
		)
		staff.invitationCode = getUniqueInvitationCode()
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