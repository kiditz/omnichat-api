package com.stafsus.api.service

import com.stafsus.api.entity.Channel
import com.stafsus.api.entity.ProductType

interface RabbitService {

	fun sendInstall(productType: ProductType, channel: Channel)
	fun sendRestart(productType: ProductType, channel: Channel)
}