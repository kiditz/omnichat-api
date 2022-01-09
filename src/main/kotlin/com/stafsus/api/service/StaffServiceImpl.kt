package com.stafsus.api.service

import com.stafsus.api.constant.MessageKey
import com.stafsus.api.dto.StaffDto
import com.stafsus.api.entity.*
import com.stafsus.api.exception.ValidationException
import com.stafsus.api.projection.StaffView
import com.stafsus.api.repository.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StaffServiceImpl(
	private val staffRepository: StaffRepository,
	private val companyService: CompanyService,
	private val userAuthorityRepository: UserAuthorityRepository,
	private val userRepository: UserRepository,
	private val channelRepository: ChannelRepository,
	private val passwordEncoder: PasswordEncoder,
	private val userService: UserService,
) : StaffService {

	@Transactional
	override fun addStaff(staffDto: StaffDto, userPrincipal: UserPrincipal): Staff {
		val company = companyService.getCompany()
		val authority = userAuthorityRepository.findByAuthority(staffDto.authority!!)
			.orElseThrow { ValidationException(MessageKey.AUTHORITY_INVALID) }
		val user = getUser(staffDto, userPrincipal)

		userService.addAuthority(Authority.valueOf(staffDto.authority), user, company)
		val staff = Staff(
			user = user,
			company = company,
			status = Status.ACTIVE,
			authority = authority,
		)
		staff.channels = getChannels(staffDto)
		val newStaff = staffRepository.findByUserEmail(staffDto.email!!).orElse(staff)
		return staffRepository.saveAndFlush(newStaff)
	}

	@Transactional
	override fun deleteStaff(id: Long): String {
		val staff = staffRepository.findById(id).orElseThrow { ValidationException(MessageKey.STAFF_NOT_FOUND) }
		staff.status = Status.INACTIVE
		return MessageKey.STAFF_DELETED_SUCCESS
	}


	private fun getUser(staffDto: StaffDto, userPrincipal: UserPrincipal): UserPrincipal {
		if (userPrincipal.email == staffDto.email) {
			throw ValidationException(MessageKey.CANNOT_USE_YOUR_OWN_EMAIL)
		}
		val user: UserPrincipal
		if (userRepository.existsByEmail(staffDto.email!!)) {
			user = userRepository.findByEmail(staffDto.email).orElse(null)
			user.email = staffDto.email
			user.name = staffDto.name!!
			user.password = passwordEncoder.encode(staffDto.password)
			user.isVerified = true
		} else {
			user = UserPrincipal(
				email = staffDto.email,
				name = staffDto.name!!,
				status = Status.ACTIVE,
				isVerified = true,
				imageUrl = userService.getPicture(staffDto.email),
				password = passwordEncoder.encode(staffDto.password)
			)
		}
		return userRepository.save(user)
	}

	private fun getChannels(staffDto: StaffDto): MutableSet<Channel> {
		return staffDto.channels.map { channelId ->
			channelRepository.findById(channelId)
				.orElseThrow { ValidationException(MessageKey.CHANNEL_WITH_ID_NOT_FOUND, listOf(channelId)) }
		}.toMutableSet()
	}

	override fun getStaffList(page: Int, size: Int): Page<StaffView> {
		val companyId = companyService.getCompanyId()
		return staffRepository.getByCompanyIdAndStatus(
			companyId,
			Status.ACTIVE,
			PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"))
		)
	}

}