package com.stafsus.api.validation

import com.stafsus.api.entity.ProductType
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class ValidProductTypeValidator : ConstraintValidator<ValidProductType, List<String>> {
	override fun isValid(value: List<String>, context: ConstraintValidatorContext?): Boolean {
		val productTypes = ProductType.values()
			.filter { it.name != "QUOTA" }
			.filter { it.name != "ADDITIONAL_USER" }
			.map { it.name }.toList()
		return !value.any { it !in productTypes }
	}

}