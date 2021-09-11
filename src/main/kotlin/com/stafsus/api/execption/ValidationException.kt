package com.stafsus.api.execption

import java.lang.RuntimeException

class ValidationException(override val message: String?) : RuntimeException(message) {
}