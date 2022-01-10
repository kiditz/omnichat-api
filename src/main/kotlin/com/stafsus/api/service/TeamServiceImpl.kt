package com.stafsus.api.service

import com.stafsus.api.constant.FtlTemplate
import com.stafsus.api.constant.MessageKey
import com.stafsus.api.dto.InvitationDto
import com.stafsus.api.dto.MailMessageDto
import com.stafsus.api.entity.Status
import com.stafsus.api.entity.Team
import com.stafsus.api.entity.UserPrincipal
import com.stafsus.api.exception.ValidationException
import com.stafsus.api.repository.TeamRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TeamServiceImpl(
	private val teamRepository: TeamRepository,
	private val companyService: CompanyService,
	private val rabbitService: RabbitService,
	private val fileService: FileService,
	@Value("\${app.invitationUrl}") val invitationUrl: String
) : TeamService {

	@Transactional
	override fun deleteStaff(id: Long): String {
		val staff = teamRepository.findById(id).orElseThrow { ValidationException(MessageKey.STAFF_NOT_FOUND) }
		staff.status = Status.INACTIVE
		return MessageKey.TEAM_DELETED_SUCCESS
	}

	@Transactional
	override fun invite(invitation: InvitationDto, user: UserPrincipal): Team {
		val company = companyService.getCompany()
		sendInvitation(user.email, company.name, invitation)
		val team = Team(
			status = Status.INACTIVE,
			email = invitation.email,
			company = company,
			picture = fileService.getIdentIcon(invitation.email!!, company.name)
		)
		return teamRepository.save(team)
	}

	private fun sendInvitation(
		email: String, companyName: String, invitation: InvitationDto
	) {
		val message = MailMessageDto(
			message = mapOf(
				"email" to email, "companyName" to companyName, "actionUrl" to "${invitationUrl}/${invitation.email}"
			),
			template = FtlTemplate.EMAIL_INVITATION,
			isHtml = true,
			subject = "$email has invited you to join them",
			to = invitation.email!!
		)
		rabbitService.sendEmail(message)
	}

}