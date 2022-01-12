package com.stafsus.api.service

import com.stafsus.api.constant.FtlTemplate
import com.stafsus.api.constant.MessageKey
import com.stafsus.api.dto.MailMessageDto
import com.stafsus.api.dto.TeamInvitationDto
import com.stafsus.api.entity.*
import com.stafsus.api.exception.ValidationException
import com.stafsus.api.repository.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TeamServiceImpl(
	private val teamRepository: TeamRepository,
	private val userCompanyRepository: UserCompanyRepository,
	private val userAuthorityRepository: UserAuthorityRepository,
	private val companyService: CompanyService,
	private val rabbitService: RabbitService,
	private val fileService: FileService,
	private val channelRepository: ChannelRepository,
	@Value("\${app.invitationUrl}") val invitationUrl: String
) : TeamService {

	@Transactional
	override fun inactive(id: Long): String {
		val staff = teamRepository.findById(id).orElseThrow { ValidationException(MessageKey.TEAM_NOT_FOUND) }
		staff.status = Status.INACTIVE
		return MessageKey.TEAM_DELETED_SUCCESS
	}

	@Transactional
	override fun invite(invitation: TeamInvitationDto, user: UserPrincipal): Team {
		val company = companyService.getCompany()
		sendInvitation(user.email, company.name, invitation)
		val team = Team(
			email = invitation.email,
			company = company,
			picture = fileService.getIdentIcon(invitation.email!!, company.name),
			authority = Authority.valueOf(invitation.authority!!)
		)
		team.channels = getChannels(invitation.channels!!)
		return teamRepository.save(team)
	}

	override fun accept(id: Long, user: UserPrincipal) {
		val team = teamRepository.findById(id).orElseThrow { ValidationException(MessageKey.TEAM_NOT_FOUND) }
		val userAuthority = userAuthorityRepository.findByAuthority(team.authority.name)
			.orElseThrow { ValidationException(MessageKey.AUTHORITY_INVALID) }
		val company = companyService.getCompany()
		val userCompany = UserCompany(
			id = UserCompanyId(
				userPrincipalId = user.id!!,
				userAuthorityId = userAuthority.id!!,
				companyId = company.id
			),
			userAuthority = userAuthority,
			company = company,
			userPrincipal = user,
		)
		userCompanyRepository.save(userCompany)
	}


	private fun getChannels(channels: List<Long>): Set<Channel> {
		return channels.map { i -> channelRepository.findById(i).orElse(null) }.toSet()
	}

	private fun sendInvitation(
		email: String, companyName: String, invitation: TeamInvitationDto
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