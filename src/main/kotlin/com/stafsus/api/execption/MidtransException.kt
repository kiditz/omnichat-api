package com.stafsus.api.execption

import java.lang.RuntimeException

class MidtransException(val messages: List<String?>?, message: String?) : RuntimeException(message) {
}