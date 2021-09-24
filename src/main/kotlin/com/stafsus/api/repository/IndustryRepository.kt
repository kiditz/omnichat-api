package com.stafsus.api.repository

import com.stafsus.api.entity.Industry
import org.springframework.data.jpa.repository.JpaRepository

interface IndustryRepository : JpaRepository<Industry, Long> {
}