package com.stafsus.api.controller

import com.stafsus.api.constant.UrlPath
import com.stafsus.api.dto.ResponseDto
import com.stafsus.api.dto.StaffDto
import com.stafsus.api.dto.UserDetailDto
import com.stafsus.api.service.StaffService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(UrlPath.STAFF)
@Tag(name = "Staff", description = "Staff API")
@Validated
class StaffController(
	private val staffService: StaffService
) {
	@PostMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	@Operation(summary = "Add new staff", security = [SecurityRequirement(name = "bearer-key")])
	fun addStaff(authentication: Authentication, @RequestBody staffDto: StaffDto): ResponseDto {
		val user = (authentication.principal as UserDetailDto).user
		val staff = staffService.addStaff(staffDto, user)
		return ResponseDto(payload = staff)
	}
}