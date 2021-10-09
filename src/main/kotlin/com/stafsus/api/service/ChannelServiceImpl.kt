package com.stafsus.api.service

import com.stafsus.api.constant.MessageKey
import com.stafsus.api.dto.ChannelDto
import com.stafsus.api.dto.ControlChannelDto
import com.stafsus.api.entity.Channel
import com.stafsus.api.entity.ProductType
import com.stafsus.api.entity.TelegramChannel
import com.stafsus.api.entity.UserPrincipal
import com.stafsus.api.exception.QuotaLimitException
import com.stafsus.api.exception.ValidationException
import com.stafsus.api.repository.ChannelRepository
import com.stafsus.api.repository.ProductRepository
import org.apache.commons.lang3.StringUtils.isEmpty
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
	private val rabbitService: RabbitService,
	private val companyService: CompanyService,
	private val fileService: FileService
) : ChannelService {

	@Transactional
	override fun addChannel(channelDto: ChannelDto): Channel {
		val product = productRepository
			.findById(channelDto.productId)
			.orElseThrow { ValidationException(MessageKey.PRODUCT_NOT_FOUND) }
		val company = companyService.getCompany()
		if (now().isAfter(company.user!!.quota!!.expiredAt)) {
			throw QuotaLimitException(MessageKey.TRIAL_TIME_IS_UP)
		}
		if (company.user!!.quota!!.maxChannel <= channelRepository.countByCompanyId(company.id!!)) {
			throw QuotaLimitException(MessageKey.MAXIMUM_CHANNEL_HAS_BEEN_REACHED)
		}
		val channel = channelDto.toEntity()

		channel.name = if (isEmpty(channelDto.name)) {
			product.name
		} else {
			channelDto.name
		}

		channel.imageUrl = if (channelDto.file == null) {
			product.imageUrl
		} else {
			val fileName = fileService.save(channelDto.file!!)
			val imageUrl = fileService.getImageUrl(fileName)
			imageUrl
		}

		channel.company = company
		channel.product = product
		channelRepository.save(channel)
		sendChannel(channel, channelDto)
		return channel
	}

	@Transactional
	override fun editChannel(channelId: Long, channelDto: ChannelDto): Channel {
		val channel =
			channelRepository.findById(channelId).orElseThrow { ValidationException(MessageKey.CHANNEL_NOT_FOUND) }
		channel.name = channelDto.name

		channel.imageUrl = if (channelDto.file == null) {
			channel.imageUrl
		} else {
			val fileName = fileService.save(channelDto.file!!)
			val imageUrl = fileService.getImageUrl(fileName)
			imageUrl
		}
		return channelRepository.save(channel)
	}


	@Transactional
	override fun controlChannel(control: ControlChannelDto, userPrincipal: UserPrincipal): Channel {
		val channel =
			channelRepository.findByDeviceId(control.deviceId!!)
				.orElseThrow { ValidationException(MessageKey.CHANNEL_NOT_FOUND) }
		if (control.active) {
			rabbitService.sendRestart(channel!!.product!!.type!!, channel)
		}
		return channelRepository.save(channel)
	}

	override fun findChannels(productId: Long, page: Int, size: Int): Page<Channel> {
		return channelRepository.findByProductId(productId, PageRequest.of(page, size, Sort.by("id").descending()))
	}


	private fun sendChannel(channel: Channel, channelDto: ChannelDto) {
		when (channel.product?.type) {
			ProductType.UNOFFICIAL_WHATSAPP -> sendInstallWa(channel)
			ProductType.TELEGRAM_BOT -> sendInstallTelegram(channelDto, channel)
			ProductType.FACEBOOK_MESSENGER -> sendInstallFacebook(channelDto, channel)
			ProductType.INSTAGRAM_DIRECT -> sendInstallInstagram(channelDto, channel)
			else -> {
			}
		}
	}

	private fun sendInstallInstagram(channelDto: ChannelDto, channel: Channel) {
		TODO("Not yet implemented")
	}

	private fun sendInstallFacebook(channelDto: ChannelDto, channel: Channel) {
		TODO("Not yet implemented")
	}

	private fun sendInstallTelegram(channelDto: ChannelDto, channel: Channel) {
		if (isEmpty(channelDto.telegramToken)) {
			throw ValidationException(MessageKey.TELEGRAM_TOKEN_REQUIRED)
		}
		channel.telegram = TelegramChannel(
			botToken = channelDto.telegramToken
		)
		channelRepository.save(channel)
		rabbitService.sendInstall(productType = ProductType.TELEGRAM_BOT, channel = channel)
	}

	private fun sendInstallWa(channel: Channel) {
		rabbitService.sendInstall(productType = ProductType.UNOFFICIAL_WHATSAPP, channel = channel)
	}
}