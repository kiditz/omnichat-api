package com.stafsus.api.service

import com.stafsus.api.constant.MessageKey
import com.stafsus.api.dto.StaffDto
import com.stafsus.api.entity.*
import com.stafsus.api.exception.ValidationException
import com.stafsus.api.repository.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StaffServiceImpl(
	private val staffRepository: StaffRepository,
	private val companyService: CompanyService,
	private val userRepository: UserRepository,
	private val userAuthorityRepository: UserAuthorityRepository,
	private val userCompanyRepository: UserCompanyRepository,
	private val channelRepository: ChannelRepository,
	private val passwordEncoder: PasswordEncoder,
	private val identIconService: IdentIconService,
	private val fileService: FileService,
) : StaffService {

	@Transactional
	override fun addStaff(staffDto: StaffDto, userPrincipal: UserPrincipal): Staff {
		val company = companyService.getCompany()
		var staff = staffRepository
			.findByEmailAndCompanyId(staffDto.email!!, company.id!!)
			.orElse(staffDto.toEntity(company))
		staff.channels.addAll(mapChannels(staffDto))
		setAuthority(staff, staffDto, userPrincipal)
		staff = staffRepository.save(staff)
		return staff
	}

	private fun setAuthority(
		staff: Staff,
		staffDto: StaffDto,
		userPrincipal: UserPrincipal
	) {
		if (!userRepository.existsByEmail(staff.email)) {
			val user = addUser(staffDto)
			addAuthority(staff, user)
		} else {
			addAuthority(staff, userPrincipal)
		}
	}

	private fun mapChannels(staffDto: StaffDto): MutableSet<Channel> {
		val channels = mutableSetOf<Channel>()
		staffDto.channels.forEach {
			val channel = channelRepository.findById(it)
				.orElseThrow {
					ValidationException(MessageKey.CHANNEL_WITH_ID_NOT_FOUND, extra = listOf(it))
				}
			channels.add(channel)
		}
		return channels
	}

	override fun getStaffList(page: Int, size: Int): Page<Staff> {
		val companyId = companyService.getCompanyId()
		return staffRepository.getByCompanyId(
			companyId,
			PageRequest.of(page, size, Sort.Direction.DESC, "id")
		)
	}

	private fun addAuthority(staff: Staff, user: UserPrincipal) {
		val authorities = staff.authority.split(",")
		authorities.forEach {
			val authority = userAuthorityRepository.findByAuthority(it).orElse(null)
			if (authority != null) {
				val userCompany = UserCompany(
					id = UserCompanyId(
						userPrincipalId = user.id,
						userAuthorityId = authority.id,
						companyId = staff.company!!.id
					),
					userAuthority = authority,
					company = staff.company,
					userPrincipal = user,
				)
				if (!userCompanyRepository.existsById(userCompany.id!!)) {
					userCompanyRepository.save(userCompany)
				}
			}
		}
	}

	private fun addUser(staff: StaffDto): UserPrincipal {
		val user = UserPrincipal(
			email = staff.email!!,
			name = "${staff.firstName} ${staff.lastName}",
			status = Status.ACTIVE,
			password = passwordEncoder.encode(staff.password),
			isVerified = false,
		)
		setProfilePicture(user)
		return userRepository.save(user)
	}

	private fun setProfilePicture(userPrincipal: UserPrincipal) {
		val icon = identIconService.saveBytes(identIconService.generateImage(userPrincipal.email, 400, 400))
		val destination = "profile"
		val image = fileService.saveOriginal(userPrincipal.email, destination, MediaType.IMAGE_PNG_VALUE, icon)
		userPrincipal.imageUrl = fileService.getImageUrl(image, destination)
	}
}