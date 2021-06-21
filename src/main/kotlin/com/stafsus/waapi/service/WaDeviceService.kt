package com.stafsus.waapi.service

import com.stafsus.waapi.entity.User

interface WaDeviceService {
    fun install(user: User)
    fun uninstall(deviceId: String, email: String)
    fun restart(deviceId: String, email: String)
}