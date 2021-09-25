package com.stafsus.api.exception

import java.lang.RuntimeException

class AccessDeniedException(override val message: String?, val args: Any?) : RuntimeException(message) {
}