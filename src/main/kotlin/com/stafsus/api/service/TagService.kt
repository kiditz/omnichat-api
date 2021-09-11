package com.stafsus.api.service

import com.stafsus.api.dto.TagFilterDto
import com.stafsus.api.entity.Tag
import com.stafsus.api.entity.UserPrincipal
import org.springframework.data.domain.Page

interface TagService {
	fun save(tag: Tag, userPrincipal: UserPrincipal): Tag
	fun delete(id: Long)
	fun findByName(tagFilterDto: TagFilterDto): Page<Tag>
}