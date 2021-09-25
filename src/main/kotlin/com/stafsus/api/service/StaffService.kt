package com.stafsus.api.service

import com.stafsus.api.dto.StaffDto
import com.stafsus.api.entity.Staff
import com.stafsus.api.entity.UserPrincipal
import org.springframework.transaction.annotation.Transactional

interface StaffService {
	fun addStaff(staffDto: StaffDto, userPrincipal: UserPrincipal): Staff
	fun acceptStaff(invitationCode: String, user: UserPrincipal): Staff
	fun declineStaff(invitationCode: String): Staff
	@Transactional(readOnly = true)
	fun checkStaffAccount(invitationCode: String): Boolean
}