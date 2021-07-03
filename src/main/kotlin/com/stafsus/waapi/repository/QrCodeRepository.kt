package com.stafsus.waapi.repository

import com.stafsus.waapi.entity.QrCode
import org.springframework.data.repository.CrudRepository

interface QrCodeRepository : CrudRepository<QrCode, String> {
}