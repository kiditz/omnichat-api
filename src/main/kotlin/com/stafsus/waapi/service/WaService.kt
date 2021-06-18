package com.stafsus.waapi.service

import com.stafsus.waapi.entity.WaDevice

interface WaService {
    fun generateDevice(deviceId: String)
}