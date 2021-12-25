package com.stafsus.api.service

import com.stafsus.api.constant.MessageKey
import com.stafsus.api.dto.StaffDto
import com.stafsus.api.entity.*
import com.stafsus.api.exception.ValidationException
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
	override fun addStaff(staffDto: StaffDto): Staff {
		val company = companyService.getCompany()
		val authority = userAuthorityRepository.findByAuthority(staffDto.authority!!)
			.orElseThrow { ValidationException(MessageKey.AUTHORITY_INVALID) }
		val user = getUser(staffDto)

		userService.addAuthority(Authority.valueOf(staffDto.authority), user, company)

		val staff = Staff(
			user = user,
			company = company,
			status = StaffStatus.ACTIVE,
			authority = authority,
		)
		staff.channels.addAll(getChannels(staffDto))
		return staffRepository.saveAndFlush(staff)
	}


	private fun getUser(staffDto: StaffDto): UserPrincipal {
		var user = UserPrincipal(
			email = staffDto.email!!,
			name = staffDto.name!!,
			status = Status.ACTIVE,
			isVerified = false,
			imageUrl = userService.getPicture(staffDto.email),
			password = passwordEncoder.encode(staffDto.password)
		)
		user = userRepository.findByEmail(staffDto.email)
			.orElse(user)
		if (!userRepository.existsByEmail(staffDto.email)) {
			userRepository.save(user)
		}
		return user
	}

	private fun getChannels(staffDto: StaffDto): MutableSet<Channel> {
		return staffDto.channels.map { channelId ->
			channelRepository.findById(channelId)
				.orElseThrow { ValidationException(MessageKey.CHANNEL_WITH_ID_NOT_FOUND, listOf(channelId)) }
		}.toMutableSet()
	}

	override fun getStaffList(page: Int, size: Int): Page<Staff> {
		val company = companyService.getCompanyId()
		return staffRepository.getByCompanyId(
			companyId = company,
			PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"))
		)
	}

}