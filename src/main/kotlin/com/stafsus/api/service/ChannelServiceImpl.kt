package com.stafsus.api.service

import com.stafsus.api.constant.MessageKey
import com.stafsus.api.dto.ChannelDto
import com.stafsus.api.dto.ControlChannelDto
import com.stafsus.api.entity.Channel
import com.stafsus.api.entity.Company
import com.stafsus.api.entity.Product
import com.stafsus.api.entity.UserPrincipal
import com.stafsus.api.exception.QuotaLimitException
import com.stafsus.api.exception.ValidationException
import com.stafsus.api.projection.ChannelProductView
import com.stafsus.api.projection.ProductChannelView
import com.stafsus.api.repository.ChannelRepository
import com.stafsus.api.repository.ProductRepository
import com.stafsus.api.service.executor.ChannelExecutor
import org.springframework.context.ApplicationContext
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime.now

@Service
class ChannelServiceImpl(
	private val channelRepository: ChannelRepository,
	private val productRepository: ProductRepository,
	private val companyService: CompanyService,
	private val fileService: FileService,
	private val applicationContext: ApplicationContext
) : ChannelService {

	@Transactional
	override fun addChannel(channelDto: ChannelDto): Channel {
		val product = productRepository
			.findById(channelDto.productId)
			.orElseThrow { ValidationException(MessageKey.PRODUCT_NOT_FOUND) }
		val company = companyService.getCompany()
		validateInstall(company)
		val channel = channelDto.toEntity()
		channel.name = channelDto.name
		channel.imageUrl = getImage(channelDto, product.imageUrl)
		setCompanyAndProduct(channel, company, product)
		val executor = getExecutor(product)
		val map = getExtra(channelDto)
		return executor.install(channel, map)
	}

	@Transactional
	override fun editChannel(channelId: Long, channelDto: ChannelDto): Channel {
		val channel = channelRepository
			.findById(channelId)
			.orElseThrow { ValidationException(MessageKey.CHANNEL_NOT_FOUND) }
		channel.name = channelDto.name
		channel.imageUrl = getImage(channelDto, channel.imageUrl!!)
		return channelRepository.save(channel)
	}

	private fun getExtra(channelDto: ChannelDto): Map<String, Any?> {
		return mapOf<String, Any?>(
			"facebookToken" to channelDto.facebookToken,
			"telegramToken" to channelDto.telegramToken,
			"instagramToken" to channelDto.instagramToken,
		)
	}

	private fun setCompanyAndProduct(
		channel: Channel,
		company: Company,
		product: Product?
	) {
		channel.company = company
		channel.product = product
	}

	private fun validateInstall(company: Company) {
		if (now().isAfter(company.quota.expiredAt)) {
			throw QuotaLimitException(MessageKey.TRIAL_TIME_IS_UP)
		}

		if (company.quota.maxChannel <= channelRepository.countByCompanyId(company.id!!)) {
			throw QuotaLimitException(MessageKey.MAXIMUM_CHANNEL_HAS_BEEN_REACHED)
		}
	}

	private fun getImage(
		channelDto: ChannelDto,
		defaultUrl: String,
	) = if (channelDto.file == null) {
		defaultUrl
	} else {
		val fileName = fileService.save(channelDto.file!!)
		val imageUrl = fileService.getImageUrl(fileName)
		imageUrl
	}

	private fun getExecutor(product: Product) =
		applicationContext.getBean(product.type!!.name, ChannelExecutor::class) as ChannelExecutor


	@Transactional
	override fun controlChannel(control: ControlChannelDto, userPrincipal: UserPrincipal): Channel {
		val channel = channelRepository.findByDeviceId(control.deviceId!!)
			.orElseThrow { ValidationException(MessageKey.CHANNEL_NOT_FOUND) }
		val executor = getExecutor(channel.product!!)
		return if (control.active) {
			executor.restart(channel)
		} else {
			executor.stop(channel)
		}
	}

	override fun findChannels(productId: Long, page: Int, size: Int): Page<Channel> {
		val companyId = companyService.getCompanyId()
		return channelRepository.findByProductIdAndCompanyId(
			productId,
			companyId,
			PageRequest.of(page, size, Sort.by("id").descending())
		)
	}

	override fun findProductsChannels(): List<ProductChannelView> {
		val companyId = companyService.getCompanyId()
		val products = productRepository.findAll(Sort.by("name"))
		val channels = products.map { product ->
			val channelViews =
				channelRepository.findByProductIdAndCompanyId(product.id!!, companyId).map { channel ->
					ChannelProductView(
						id = channel.id!!,
						channel.name,
						imageUrl = channel.imageUrl!!,
					)
				}
			ProductChannelView(
				id = product.id,
				name = product.name,
				imageUrl = product.imageUrl,
				channels = channelViews
			)
		}.toList()
		return channels
	}
}