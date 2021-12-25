package com.stafsus.api.exception

import java.lang.RuntimeException

class MidtransException(val messages: List<String?>?, message: String?) : RuntimeException(message) {
}