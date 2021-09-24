package com.stafsus.api.exception

import java.lang.RuntimeException

class QuotaLimitException(override val message: String?) : RuntimeException(message) {
}