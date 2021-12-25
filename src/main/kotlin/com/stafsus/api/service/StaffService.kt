package com.stafsus.api.service

import com.stafsus.api.dto.StaffDto
import com.stafsus.api.entity.Staff
import com.stafsus.api.entity.UserPrincipal
import org.springframework.data.domain.Page
import org.springframework.transaction.annotation.Transactional

interface StaffService {
	fun getStaffList(page: Int, size: Int): Page<Staff>
	fun addStaff(staffDto: StaffDto): Staff
}