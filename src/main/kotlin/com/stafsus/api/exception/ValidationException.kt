package com.stafsus.api.exception

import java.lang.RuntimeException

class ValidationException(override val message: String?, val extraData: List<Any>? = null) : RuntimeException(message) {
}