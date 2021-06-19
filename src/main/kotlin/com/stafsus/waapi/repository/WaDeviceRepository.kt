package com.stafsus.waapi.repository

import com.stafsus.waapi.entity.WaDevice
import org.springframework.data.repository.CrudRepository

interface WaDeviceRepository : CrudRepository<WaDevice, Long>