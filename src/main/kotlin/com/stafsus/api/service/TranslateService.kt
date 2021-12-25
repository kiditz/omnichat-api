package com.stafsus.api.service

import org.springframework.context.MessageSourceResolvable

interface TranslateService {
    fun toLocale(msgCode: String): String
    fun toLocale(msgCode: String, args: List<Any>?): String
    fun getMessage(resolveable: MessageSourceResolvable): String
}