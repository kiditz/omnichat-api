package com.stafsus.waapi.service

import com.stafsus.waapi.entity.User

interface WaService {
    fun sendDeviceToQueue(user: User, deviceId: String)
}