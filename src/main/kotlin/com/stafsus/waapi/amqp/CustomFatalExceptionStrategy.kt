package com.stafsus.waapi.amqp

import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler.DefaultExceptionStrategy

class CustomFatalExceptionStrategy : DefaultExceptionStrategy() {
    override fun isFatal(t: Throwable): Boolean {
        return t.cause == null
    }
}