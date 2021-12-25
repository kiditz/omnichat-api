package com.stafsus.api.service

import org.springframework.context.MessageSource
import org.springframework.context.MessageSourceResolvable
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Service


@Service
class TranslateServiceImpl(private val messageSource: MessageSource) : TranslateService {

	override fun toLocale(msgCode: String): String {
		val locale = LocaleContextHolder.getLocale()
		return try {
			messageSource.getMessage(msgCode, null, locale)
		} catch (ex: Exception) {
			msgCode
		}
	}

	override fun getMessage(resolveable: MessageSourceResolvable): String {
		val locale = LocaleContextHolder.getLocale()
		return messageSource.getMessage(resolveable, locale)
	}

	override fun toLocale(msgCode: String, args: List<Any>?): String {
		val locale = LocaleContextHolder.getLocale()
		return messageSource.getMessage(msgCode, args?.toTypedArray(), locale)
	}
}