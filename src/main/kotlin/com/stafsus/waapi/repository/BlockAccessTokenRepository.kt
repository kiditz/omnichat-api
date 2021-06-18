package com.stafsus.waapi.repository

import com.stafsus.waapi.entity.BlockAccessToken
import org.springframework.data.repository.CrudRepository

interface BlockAccessTokenRepository : CrudRepository<BlockAccessToken, Long> {
    fun existsByToken(token: String?): Boolean
}