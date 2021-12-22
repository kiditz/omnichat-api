package com.stafsus.api.repository

import com.stafsus.api.entity.Quota
import org.springframework.data.repository.CrudRepository

interface QuotaRepository : CrudRepository<Quota, Long> {
}