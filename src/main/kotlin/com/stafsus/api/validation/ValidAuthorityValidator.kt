package com.stafsus.api.validation

import com.stafsus.api.entity.Authority
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class ValidAuthorityValidator : ConstraintValidator<ValidAuthority, List<String>> {
	override fun isValid(value: List<String>, context: ConstraintValidatorContext?): Boolean {
		val authorities = Authority.values().map { it.name }.toList()
		return value.any { it in authorities }
	}

}