package com.stafsus.waapi.service.dto

data class QrCodeDto(
        val deviceId: String,
        val qrCode: String,
        val accessedBy: String
)