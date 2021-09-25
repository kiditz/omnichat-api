package com.stafsus.api.service

import com.stafsus.api.constant.MessageKey
import com.stafsus.api.dto.TagFilterDto
import com.stafsus.api.entity.Tag
import com.stafsus.api.entity.UserPrincipal
import com.stafsus.api.exception.ValidationException
import com.stafsus.api.repository.TagRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TagServiceImpl(
	private val tagRepository: TagRepository,
	private val companyService: CompanyService
) : TagService {
	@Transactional
	override fun save(tag: Tag, userPrincipal: UserPrincipal): Tag {
		tag.company = companyService.getCompany()
		return tagRepository.save(tag)
	}

	@Transactional
	override fun delete(id: Long) {
		if (!tagRepository.existsById(id)) {
			throw ValidationException(MessageKey.TAG_NOT_FOUND)
		}
		tagRepository.deleteById(id)
	}

	override fun findByName(tagFilterDto: TagFilterDto): Page<Tag> {
		val companyId = companyService.getCompany().id!!
		return tagRepository.findByNameContainingAndCompanyId(
			tagFilterDto.name, companyId, PageRequest.of(tagFilterDto.page, tagFilterDto.size).withSort(
				Sort.by("name").ascending()
			)
		)
	}

}