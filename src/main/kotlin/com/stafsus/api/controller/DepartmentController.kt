package com.stafsus.api.controller

import com.stafsus.api.constant.MessageKey
import com.stafsus.api.constant.UrlPath
import com.stafsus.api.dto.DepartmentDto
import com.stafsus.api.dto.DepartmentFilterDto
import com.stafsus.api.dto.ResponseDto
import com.stafsus.api.dto.UserDetailDto
import com.stafsus.api.service.DepartmentService
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
@RequestMapping(UrlPath.DEPARTMENT)
@Tag(name = "Department", description = "Department API")
@Validated
class DepartmentController(
	private val departmentService: DepartmentService,
	private val translateService: TranslateService
) {
	@PostMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	@Operation(summary = "Add Department", security = [SecurityRequirement(name = "bearer-key")])
	fun addDepartment(@Valid @RequestBody request: DepartmentDto, authentication: Authentication): ResponseDto {
		val user = (authentication.principal as UserDetailDto).user
		val department = departmentService.save(department = request.toEntity(), user)
		return ResponseDto(payload = department)
	}

	@PutMapping("{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
	@Operation(summary = "Update Department", security = [SecurityRequirement(name = "bearer-key")])
	fun updateDepartment(
		@PathVariable id: Long,
		@Valid @RequestBody request: DepartmentDto,
		authentication: Authentication
	): ResponseDto {
		val user = (authentication.principal as UserDetailDto).user
		val department = departmentService.save(department = request.toEntity().copy(id = id), user)
		return ResponseDto(payload = department)
	}

	@GetMapping
	@Operation(summary = "Get All Department", security = [SecurityRequirement(name = "bearer-key")])
	fun findDepartment(
		@RequestParam(required = false) name: String?,
		@RequestParam page: Int,
		@RequestParam size: Int,
		authentication: Authentication
	): ResponseDto {
		val departmentPage = departmentService.findByName(DepartmentFilterDto(name, page, size))
		return ResponseDto.fromPage(departmentPage)
	}


	@DeleteMapping("{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
	@Operation(summary = "Delete Department", security = [SecurityRequirement(name = "bearer-key")])
	fun deleteDepartment(@PathVariable id: Long): ResponseDto {
		departmentService.delete(id)
		return ResponseDto(
			message = translateService.toLocale(MessageKey.DELETE_DEPARTMENT_SUCCESS),
			key = MessageKey.DELETE_DEPARTMENT_SUCCESS
		)
	}
}