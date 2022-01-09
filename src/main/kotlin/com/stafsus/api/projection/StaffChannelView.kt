package com.stafsus.api.projection

import org.springframework.beans.factory.annotation.Value

interface StaffChannelView {
	@Value("#{target.product.id}")
	fun getId(): String

	@Value("#{target.product.imageUrl}")
	fun getProductImage(): String

	@Value("#{target.product.name}")
	fun getProductName(): String
}
