package com.stafsus.api.utils

class PhoneUtils private constructor() {
	companion object {
		fun sanitize(phone: String): String {

			var phoneNumber = phone
			when {
				phoneNumber.startsWith("021") -> phoneNumber = phone
				phoneNumber.startsWith("08") -> phoneNumber = phone.replaceFirst("08", "62")
				phoneNumber.startsWith("+") -> phoneNumber = phone.replaceFirst("+", "")
			}
			return phoneNumber.replace("-", "")
		}
	}
}