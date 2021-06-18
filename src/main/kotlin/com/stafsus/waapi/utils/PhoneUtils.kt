package com.stafsus.waapi.utils

class PhoneUtils private constructor() {
    companion object {
        fun sanitize(phone: String): String {
            var phoneNumber = phone
            when {
                phoneNumber.startsWith("021") -> phoneNumber = phone
                phoneNumber.startsWith("0") -> phoneNumber = phone.replaceFirst("0", "+62")
                phoneNumber.startsWith("8") -> phoneNumber = phone.replaceFirst("8", "+628")
                phoneNumber.startsWith("62") -> {
                    phoneNumber = phone.replaceFirst("62", "+62")
                }
            }
            return phoneNumber.replace("-", "")
        }
    }
}