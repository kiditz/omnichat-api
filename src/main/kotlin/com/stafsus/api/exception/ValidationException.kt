package com.stafsus.api.exception

import java.lang.RuntimeException

class ValidationException(override val message: String?, val extra: List<Long>? = null) :
	RuntimeException(message) {
}