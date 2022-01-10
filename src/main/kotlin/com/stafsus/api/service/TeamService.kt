package com.stafsus.api.service

import com.stafsus.api.dto.InvitationDto
import com.stafsus.api.entity.Team
import com.stafsus.api.entity.UserPrincipal

interface TeamService {
	fun invite(invitation: InvitationDto, user: UserPrincipal): Team
	fun deleteStaff(id: Long): String
}