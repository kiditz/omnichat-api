package com.stafsus.api.validation

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class ValueOfEnumValidator : ConstraintValidator<ValueOfEnum, CharSequence> {
	private lateinit var acceptedValues: List<String>

	override fun initialize(annotation: ValueOfEnum) {
		acceptedValues = annotation.enumClass.java.enumConstants.map { it.name }.toList()
		super.initialize(annotation)
	}

	override fun isValid(value: CharSequence?, context: ConstraintValidatorContext?): Boolean {
		if (value == null) return true
		return acceptedValues.contains(value)
	}

}