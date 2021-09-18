package com.stafsus.api.service

import com.stafsus.api.dto.StaffDto
import com.stafsus.api.entity.Staff
import com.stafsus.api.entity.Status
import com.stafsus.api.entity.UserPrincipal
import com.stafsus.api.repository.StaffRepository
import com.stafsus.api.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StaffServiceImpl(
	private val staffRepository: StaffRepository,
	private val userRepository: UserRepository
) : StaffService {

	@Transactional
	override fun addStaff(staffDto: StaffDto, userPrincipal: UserPrincipal): Staff {
		val user = UserPrincipal(
			email = staffDto.email!!,
			businessName = userPrincipal.businessName,
			authorities = setOf(staffDto.authority!!),
			status = Status.INACTIVE,
			parentId = userPrincipal.id,
			password = ""
		)
		userRepository.save(user)
		val staff = staffDto.toEntity(user)
		return staffRepository.save(staff)
	}
}