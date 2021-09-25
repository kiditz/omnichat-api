package com.stafsus.api.service

import com.stafsus.api.config.ThreadLocalStorage
import com.stafsus.api.constant.MessageKey
import com.stafsus.api.dto.SignUpDto
import com.stafsus.api.dto.StaffSignUpDto
import com.stafsus.api.dto.UserDetailDto
import com.stafsus.api.entity.*
import com.stafsus.api.exception.ValidationException
import com.stafsus.api.repository.*
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class UserServiceImpl(
	private val userRepository: UserRepository,
	private val priceRepository: PriceRepository,
	private val industryRepository: IndustryRepository,
	private val companyRepository: CompanyRepository,
	private val userAuthorityRepository: UserAuthorityRepository,
	private val userCompanyRepository: UserCompanyRepository,
	private val passwordEncoder: PasswordEncoder,
	private val identIconService: IdentIconService,
	private val staffRepository: StaffRepository,
	private val fileService: FileService,

	) : UserService {
	private val log = LoggerFactory.getLogger(javaClass)

	@Transactional
	override fun signUp(signUpDto: SignUpDto): UserPrincipal {
		val userPrincipal = signUpDto.toUser()
		if (userRepository.existsByEmail(userPrincipal.email)) {
			throw ValidationException(MessageKey.EMAIL_EXISTS)
		}
		setPassword(userPrincipal)
		setProfilePicture(userPrincipal)
		setQuota(userPrincipal)
		userRepository.save(userPrincipal)
		val industry = industryRepository.findById(signUpDto.industryId!!)
			.orElseThrow { ValidationException(MessageKey.INDUSTRY_NOT_FOUND) }
		val company = addCompany(signUpDto, industry, userPrincipal)
		addAuthority(userPrincipal, company)
		return userPrincipal
	}

	private fun setPassword(userPrincipal: UserPrincipal) {
		userPrincipal.password = passwordEncoder.encode(userPrincipal.password)
	}

	private fun setQuota(userPrincipal: UserPrincipal) {
		val price =
			priceRepository.findByName(Price.TRIAL).orElseThrow { ValidationException(MessageKey.PRICE_NOT_FOUND) }
		userPrincipal.quota = Quota(
			maxAgent = price.agentAmount!!.toInt(),
			maxChannel = price.channelAmount!!.toInt(),
			monthlyActiveVisitor = price.monthlyActiveVisitor,
			expiredAt = LocalDateTime.now().plusDays(price.accessTime)
		)
	}

	private fun addCompany(
		signUpDto: SignUpDto,
		industry: Industry?,
		userPrincipal: UserPrincipal
	): Company {
		val company = Company(
			name = signUpDto.companyName!!,
			industry = industry,
			user = userPrincipal
		)
		companyRepository.save(company)
		return company
	}

	private fun addAuthority(userPrincipal: UserPrincipal, company: Company) {
		val userAuthority = userAuthorityRepository.findByAuthority(Authority.ADMIN.name).orElse(null)
		val userCompany = UserCompany(
			userPrincipal = userPrincipal,
			userAuthority = userAuthority,
			company = company
		)
		userCompanyRepository.save(userCompany)
	}

	@Transactional
	override fun invitationSignUp(signUpDto: StaffSignUpDto): UserPrincipal {
		val staff = staffRepository.findByInvitationCode(signUpDto.invitationCode!!)
			.orElseThrow { ValidationException(MessageKey.STAFF_NOT_FOUND) }
		val userPrincipal = signUpDto.toUser(staff.email)
		if (userRepository.existsByEmail(userPrincipal.email)) {
			throw ValidationException(MessageKey.EMAIL_EXISTS)
		}
		setPassword(userPrincipal)
		setProfilePicture(userPrincipal)
		userRepository.saveAndFlush(userPrincipal)
		val authorities = staff.authority.split(",")
		authorities.forEach {
			val authority = userAuthorityRepository.findByAuthority(it).orElse(null)
			if (authority != null) {
				val userCompany = UserCompany(
					id = UserCompanyId(
						userPrincipal.id,
						staff.company!!.id,
						authority.id
					),
					userAuthority = authority,
					company = staff.company,
					userPrincipal = userPrincipal
				)
				userCompanyRepository.save(userCompany)
			}
		}
		staff.status = StaffStatus.ACTIVE
		staffRepository.save(staff)
		return userPrincipal
	}


	private fun setProfilePicture(userPrincipal: UserPrincipal) {
		val icon = identIconService.saveBytes(identIconService.generateImage(userPrincipal.email, 400, 400))
		val destination = "profile"
		val image = fileService.saveOriginal(userPrincipal.email, destination, MediaType.IMAGE_PNG_VALUE, icon)
		userPrincipal.imageUrl = fileService.getImageUrl(image, destination)
	}

	override fun loadUserByUsername(username: String): UserDetails {
		val user = userRepository.findByEmail(username).orElseThrow { ValidationException(MessageKey.USER_NOT_FOUND) }
		return UserDetailDto(user, setOf())
	}

	override fun loadUserById(id: Long): UserDetails {
		val user = userRepository.findById(id).orElseThrow { ValidationException(MessageKey.USER_NOT_FOUND) }
		log.info("Tenant ID: {}", ThreadLocalStorage.getTenantId())
		val tenantId = ThreadLocalStorage.getTenantId() ?: -99
		val userCompanies =
			userCompanyRepository.getByUserPrincipalIdAndCompanyId(user.id!!, tenantId)
		val authorities = userCompanies.map { Authority.valueOf(it.userAuthority!!.authority) }.toSet()
		return UserDetailDto(user, authorities)
	}
}