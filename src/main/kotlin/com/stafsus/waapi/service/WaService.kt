package com.stafsus.waapi.service

import com.stafsus.waapi.entity.User

interface WaService {
    fun deployDevice(user: User, deviceId: String)
}