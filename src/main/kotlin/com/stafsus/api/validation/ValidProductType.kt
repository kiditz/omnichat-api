package com.stafsus.api.validation

import com.stafsus.api.constant.MessageKey
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@MustBeDocumented
@Constraint(validatedBy = [ValidProductTypeValidator::class])
annotation class ValidProductType(
	val message: String = "{${MessageKey.PRODUCT_TYPE_INVALID}}",
	val groups: Array<KClass<*>> = [],
	val payload: Array<KClass<out Payload>> = []
)