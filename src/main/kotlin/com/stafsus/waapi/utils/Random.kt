package com.stafsus.waapi.utils

class Random {
    companion object {
        fun string(length: Int): String {
            val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
            return (1..length)
                    .map { allowedChars.random() }
                    .joinToString("")
        }

        fun stringLowerOnly(length: Int): String {
            val allowedChars = ('a'..'z')
            return (1..length)
                    .map { allowedChars.random() }
                    .joinToString("")
        }
    }
}