package com.stafsus.api.service

import com.stafsus.api.config.ThreadLocalStorage
import com.stafsus.api.constant.MessageKey
import com.stafsus.api.dto.EditUserDto
import com.stafsus.api.dto.SignUpDto
import com.stafsus.api.dto.UserDetailDto
import com.stafsus.api.entity.*
import com.stafsus.api.exception.ValidationException
import com.stafsus.api.repository.*
import org.apache.commons.lang3.RandomStringUtils
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

@Service
class UserServiceImpl(
	private val userRepository: UserRepository,
	private val priceRepository: PriceRepository,
	private val industryRepository: IndustryRepository,
	private val companyRepository: CompanyRepository,
	private val userAuthorityRepository: UserAuthorityRepository,
	private val userCompanyRepository: UserCompanyRepository,
	private val quotaRepository: QuotaRepository,
	private val apiKeyRepository: ApiKeyRepository,
	private val passwordEncoder: PasswordEncoder,
	private val identIconService: IdentIconService,
	private val fileService: FileService,

	) : UserService {
	private val log = LoggerFactory.getLogger(javaClass)

	@Transactional
	override fun signUp(signUpDto: SignUpDto): UserPrincipal {
		val userPrincipal = signUpDto.toUser()
		if (userRepository.existsByEmail(userPrincipal.email)) {
			throw ValidationException(MessageKey.EMAIL_EXISTS)
		}
		userPrincipal.imageUrl = getPicture(signUpDto.email!!)
		setPassword(userPrincipal)
		userRepository.save(userPrincipal)
		val industry = industryRepository.findById(signUpDto.industryId!!)
			.orElseThrow { ValidationException(MessageKey.INDUSTRY_NOT_FOUND) }
		val company = addCompany(signUpDto, industry, userPrincipal)
		addAuthority(Authority.ADMIN, userPrincipal, company)
		addApiKey(company)
		return userPrincipal
	}

	private fun addApiKey(company: Company) {
		val apiKey = ApiKey(
			serverKey = RandomStringUtils.randomAlphanumeric(60),
			appId = RandomStringUtils.randomAlphanumeric(20),
			company = company
		)
		apiKeyRepository.save(apiKey)
	}

	private fun setPassword(userPrincipal: UserPrincipal) {
		userPrincipal.password = passwordEncoder.encode(userPrincipal.password)
	}

	override fun getTrialQuota(): Quota {
		val price =
			priceRepository.findByName(Price.TRIAL).orElseThrow { ValidationException(MessageKey.PRICE_NOT_FOUND) }
		val quota = Quota(
			maxAgent = price.agentAmount!!.toInt(),
			maxChannel = price.channelAmount!!.toInt(),
			monthlyActiveVisitor = price.monthlyActiveVisitor,
			expiredAt = LocalDateTime.now().plusDays(price.accessTime)
		)
		return quotaRepository.save(quota)
	}

	private fun addCompany(
		signUpDto: SignUpDto,
		industry: Industry?,
		userPrincipal: UserPrincipal
	): Company {
		val company = Company(
			name = signUpDto.companyName!!,
			industry = industry!!,
			quota = getTrialQuota(),
			user = userPrincipal,
		)
		company.createdBy = userPrincipal.email
		company.updatedBy = userPrincipal.email
		companyRepository.save(company)
		return company
	}

	override fun addAuthority(authority: Authority, userPrincipal: UserPrincipal, company: Company) {
		val userAuthority = userAuthorityRepository.findByAuthority(authority.name).orElse(null)
		val userCompany = UserCompany(
			UserCompanyId(
				userPrincipalId = userPrincipal.id,
				userAuthorityId = userAuthority.id,
				companyId = company.id,
			),
			userPrincipal = userPrincipal,
			userAuthority = userAuthority,
			company = company
		)
		userCompanyRepository.save(userCompany)
	}

	override fun getPicture(email: String): String {
		val icon = identIconService.saveBytes(identIconService.generateImage(email, 400, 400))
		val destination = "profile"
		val image = fileService.saveOriginal(email, destination, MediaType.IMAGE_PNG_VALUE, icon)
		return fileService.getImageUrl(image, destination)
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


	@Transactional
	override fun editUser(editUserDto: EditUserDto, user: UserPrincipal): UserPrincipal {
		user.email = editUserDto.email!!
		user.name = editUserDto.name!!
		if (StringUtils.isNotEmpty(editUserDto.oldPassword) && StringUtils.isNotEmpty(editUserDto.newPassword)) {
			if (!passwordEncoder.matches(editUserDto.oldPassword, user.password)) {
				throw ValidationException(MessageKey.INVALID_EMAIL_OR_PASSWORD)
			}
			user.password = passwordEncoder.encode(editUserDto.newPassword!!)
		}
		return userRepository.save(user)
	}

	override fun updateImage(user: UserPrincipal, multipartFile: MultipartFile): UserPrincipal {
		val destination = "profile"
		val image = fileService.saveOriginal(destination, multipartFile)
		user.imageUrl = fileService.getImageUrl(image, destination)
		return userRepository.save(user)
	}
}