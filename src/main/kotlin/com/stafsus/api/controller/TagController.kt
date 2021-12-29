package com.stafsus.api.controller

import com.stafsus.api.constant.MessageKey
import com.stafsus.api.constant.UrlPath
import com.stafsus.api.dto.*
import com.stafsus.api.service.TagService
import com.stafsus.api.service.TranslateService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping(UrlPath.API_TAG)
@Tag(name = "Tag", description = "Tag API")
@Validated
class TagController(
	private val tagService: TagService,
	private val translateService: TranslateService
) {
	@PostMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	@Operation(summary = "Add Tag", security = [SecurityRequirement(name = "bearer-key")])
	fun addTag(@Valid @RequestBody request: TagDto, authentication: Authentication): ResponseDto {
		val user = (authentication.principal as UserDetailDto).user
		val tag = tagService.save(request.toEntity(), user)
		return ResponseDto(payload = tag)
	}

	@PutMapping("{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	@Operation(summary = "Update Tag", security = [SecurityRequirement(name = "bearer-key")])
	fun updateTag(
		@PathVariable id: Long,
		@Valid @RequestBody request: TagDto,
		authentication: Authentication
	): ResponseDto {
		val user = (authentication.principal as UserDetailDto).user
		val tag = tagService.save(tag = request.toEntity().copy(id = id), user)
		return ResponseDto(payload = tag)
	}

	@GetMapping
	@Operation(summary = "Find Tag", security = [SecurityRequirement(name = "bearer-key")])
	fun findTag(
		@RequestParam(required = false) name: String?,
		@RequestParam page: Int,
		@RequestParam size: Int,
		authentication: Authentication
	): ResponseDto {
		val user = (authentication.principal as UserDetailDto).user
		val tagPage = tagService.findByName(TagFilterDto(user, name, page, size))
		return ResponseDto.fromPage(tagPage)
	}


	@DeleteMapping("{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	@Operation(summary = "Delete Tag", security = [SecurityRequirement(name = "bearer-key")])
	fun deleteTag(@PathVariable id: Long): ResponseDto {
		tagService.delete(id)
		return ResponseDto(
			message = translateService.toLocale(MessageKey.DELETE_DEPARTMENT_SUCCESS),
			key = MessageKey.DELETE_DEPARTMENT_SUCCESS
		)
	}
}