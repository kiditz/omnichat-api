package com.stafsus.api.service

import com.stafsus.api.dto.StaffDto
import com.stafsus.api.entity.Staff
import com.stafsus.api.projection.StaffView
import org.springframework.data.domain.Page

interface StaffService {
	fun getStaffList(page: Int, size: Int): Page<StaffView>
	fun addStaff(staffDto: StaffDto): Staff
	fun deleteStaff(id: Long): String
}