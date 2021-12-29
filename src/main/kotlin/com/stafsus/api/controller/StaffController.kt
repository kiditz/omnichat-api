package com.stafsus.api.controller

import com.stafsus.api.constant.UrlPath
import com.stafsus.api.dto.ResponseDto
import com.stafsus.api.dto.StaffDto
import com.stafsus.api.service.StaffService
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
@RequestMapping(UrlPath.API_STAFF)
@Tag(name = "Staff", description = "Staff API")
@Validated
class StaffController(
	private val staffService: StaffService,
	private val translateService: TranslateService,
) {
	@PostMapping
	@PreAuthorize("hasAuthority('ADMIN', 'SUPERVISOR')")
	@Operation(summary = "Add new staff", security = [SecurityRequirement(name = "bearer-key")])
	fun addStaff(authentication: Authentication, @Valid @RequestBody staffDto: StaffDto): ResponseDto {
		val staff = staffService.addStaff(staffDto)
		return ResponseDto(payload = staff)
	}

	@GetMapping
	@PreAuthorize("hasAnyAuthority('ADMIN', 'SUPERVISOR')")
	@Operation(summary = "Staff list", security = [SecurityRequirement(name = "bearer-key")])
	fun getStaffList(
		@RequestParam page: Int,
		@RequestParam size: Int,
	): ResponseDto {
		val staffs = staffService.getStaffList(page, size)
		return ResponseDto.fromPage(staffs)
	}


	@DeleteMapping("{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'SUPERVISOR')")
	@Operation(summary = "Delete Staff", security = [SecurityRequirement(name = "bearer-key")])
	fun deleteStaff(
		@PathVariable id: Long,
	): ResponseDto {
		val message = staffService.deleteStaff(id)
		return ResponseDto(message = translateService.toLocale(message), success = true)
	}
}