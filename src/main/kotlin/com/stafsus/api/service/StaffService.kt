package com.stafsus.api.service

import com.stafsus.api.dto.StaffDto
import com.stafsus.api.entity.Staff
import com.stafsus.api.entity.UserPrincipal

interface StaffService {
	fun addStaff(staffDto: StaffDto, userPrincipal: UserPrincipal): Staff
	fun getStaffList(page: Int, size: Int): List<Staff>
}