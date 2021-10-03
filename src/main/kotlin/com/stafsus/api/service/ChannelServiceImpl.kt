package com.stafsus.api.service

import com.stafsus.api.constant.MessageKey
import com.stafsus.api.dto.ChannelDto
import com.stafsus.api.entity.Channel
import com.stafsus.api.entity.UserPrincipal
import com.stafsus.api.exception.QuotaLimitException
import com.stafsus.api.exception.ValidationException
import com.stafsus.api.repository.ChannelRepository
import com.stafsus.api.repository.ProductRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ChannelServiceImpl(
	private val channelRepository: ChannelRepository,
	private val productRepository: ProductRepository,
	private val rabbitService: RabbitService,
	private val companyService: CompanyService
) : ChannelService {
	@Transactional
	override fun install(channelDto: ChannelDto, userPrincipal: UserPrincipal): Channel {
		val product = productRepository
			.findById(channelDto.productId!!)
			.orElseThrow { QuotaLimitException(MessageKey.PRODUCT_NOT_FOUND) }
		val company = companyService.getCompany()
		if (LocalDateTime.now().isAfter(company.user!!.quota!!.expiredAt)) {
			throw QuotaLimitException(MessageKey.TRIAL_TIME_IS_UP)
		}
		if (company.user!!.quota!!.maxChannel < channelRepository.countByCompanyId(company.id!!)) {
			throw QuotaLimitException(MessageKey.MAXIMUM_CHANNEL_HAS_BEEN_REACHED)
		}
		val channel = channelDto.toEntity()
		channel.company = company
		channel.product = product
		channelRepository.save(channel)
		rabbitService.sendInstall(product.type!!, channel)
		return channel
	}


	@Transactional
	override fun restart(deviceId: String, userPrincipal: UserPrincipal): Channel {
		val channel =
			channelRepository.findByDeviceId(deviceId).orElseThrow { ValidationException(MessageKey.CHANNEL_NOT_FOUND) }
		channel.isActive = false
		channel.isPending = true
		channel.isOnline = false
		rabbitService.sendRestart(channel!!.product!!.type!!, channel)
		return channelRepository.save(channel)
	}

	override fun findChannels(page: Int, size: Int): Page<Channel> {
		val company = companyService.getCompany()
		return channelRepository.findByCompanyId(company.id!!, PageRequest.of(page, size, Sort.by("id").descending()))
	}


}