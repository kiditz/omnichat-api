package com.stafsus.api.validation

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@Target(FIELD, CLASS, CONSTRUCTOR)
@MustBeDocumented
@Constraint(validatedBy = [ValueOfEnumValidator::class])
annotation class ValueOfEnum(
	val enumClass: KClass<out Enum<*>>,
	val message: String = "must be any of enum {enumClass}",
	val groups: Array<KClass<*>> = [],
	val payload: Array<KClass<out Payload>> = []
)

