package com.stafsus.api.execption

import java.lang.RuntimeException

class QuotaLimitException(override val message: String?) : RuntimeException(message) {
}