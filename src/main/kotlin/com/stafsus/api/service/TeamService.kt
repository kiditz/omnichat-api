package com.stafsus.api.service

import com.stafsus.api.dto.TeamInvitationDto
import com.stafsus.api.entity.Team
import com.stafsus.api.entity.UserPrincipal

interface TeamService {
	fun invite(invitation: TeamInvitationDto, user: UserPrincipal): Team
	fun accept(id: Long, user: UserPrincipal)
	fun inactive(id: Long): String
}