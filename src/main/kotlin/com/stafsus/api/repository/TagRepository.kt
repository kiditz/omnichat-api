package com.stafsus.api.repository

import com.stafsus.api.entity.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface TagRepository : JpaRepository<Tag, Long> {
	fun findByNameContainingAndUserId(name: String?, userId: Long, pageable: Pageable): Page<Tag>
}