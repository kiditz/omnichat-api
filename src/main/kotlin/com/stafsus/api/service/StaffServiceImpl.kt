package com.stafsus.api.service

import com.stafsus.api.dto.StaffDto
import com.stafsus.api.entity.Staff
import com.stafsus.api.entity.Status
import com.stafsus.api.entity.UserCompany
import com.stafsus.api.entity.UserPrincipal
import com.stafsus.api.repository.StaffRepository
import com.stafsus.api.repository.UserAuthorityRepository
import com.stafsus.api.repository.UserCompanyRepository
import com.stafsus.api.repository.UserRepository
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
		val user = addUser(staffDto, userPrincipal)
		staff = staffRepository.save(staff)
		addAuthority(staff, user)
		return staff
	}

	private fun addAuthority(staff: Staff, user: UserPrincipal) {
		val authorities = staff.authority.split(",")
		authorities.forEach {
			val authority = userAuthorityRepository.findByAuthority(it).orElse(null)
			if (authority != null) {
				val userCompany = UserCompany(
					userAuthority = authority,
					company = staff.company,
					userPrincipal = user,
				)
				userCompanyRepository.save(userCompany)
			}
		}
	}

	private fun addUser(staff: StaffDto, userPrincipal: UserPrincipal): UserPrincipal {
		val user = UserPrincipal(
			email = staff.email!!,
			name = "${staff.firstName} ${staff.lastName}",
			status = Status.ACTIVE,
			password = passwordEncoder.encode(staff.password),
			isVerified = false,
			quota = userPrincipal.quota
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