package com.stafsus.waapi.service

import org.springframework.context.MessageSourceResolvable

interface TranslateService {
    fun toLocale(msgCode: String): String
    fun toLocale(msgCode: String, args: Array<Any?>?): String
    fun getMessage(resolveable: MessageSourceResolvable): String
}