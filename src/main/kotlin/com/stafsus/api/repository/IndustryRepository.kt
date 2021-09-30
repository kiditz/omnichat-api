package com.stafsus.api.repository

import com.stafsus.api.entity.Industry
import com.stafsus.api.projection.IndustryProjection
import org.springframework.data.jpa.repository.JpaRepository

interface IndustryRepository : JpaRepository<Industry, Long> {
	fun findByOrderByName(): List<IndustryProjection>
}