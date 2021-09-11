package com.stafsus.api.service

import com.stafsus.api.constant.MessageKey
import com.stafsus.api.dto.ChannelDto
import com.stafsus.api.entity.Channel
import com.stafsus.api.entity.UserPrincipal
import com.stafsus.api.execption.ValidationException
import com.stafsus.api.repository.ChannelRepository
import com.stafsus.api.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChannelServiceImpl(
	private val channelRepository: ChannelRepository,
	private val productRepository: ProductRepository,
	private val rabbitService: RabbitService,
) : ChannelService {
	@Transactional
	override fun install(channelDto: ChannelDto, userPrincipal: UserPrincipal): Channel {
		val product = productRepository.findById(channelDto.productId!!)
			.orElseThrow { ValidationException(MessageKey.PRODUCT_NOT_FOUND) }

		val channel = channelDto.toEntity()
		channel.user = userPrincipal
		channel.product = product
		channelRepository.save(channel)
		rabbitService.sendInstall(product.type!!, channel)
		return channel
	}
}