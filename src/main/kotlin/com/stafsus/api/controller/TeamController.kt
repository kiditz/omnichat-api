package com.stafsus.api.controller

import com.stafsus.api.constant.UrlPath
import com.stafsus.api.dto.ResponseDto
import com.stafsus.api.dto.TeamInvitationDto
import com.stafsus.api.dto.UserDetailDto
import com.stafsus.api.service.TeamService
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
@RequestMapping(UrlPath.API_TEAM)
@Tag(name = "Team", description = "Team API")
@Validated
class TeamController(
	private val staffService: TeamService,
	private val translateService: TranslateService,
) {
	@PostMapping(UrlPath.INVITE)
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@Operation(summary = "Invite new member for your team", security = [SecurityRequirement(name = "bearer-key")])
	fun invite(authentication: Authentication, @Valid @RequestBody invitation: TeamInvitationDto): ResponseDto {
		val user = (authentication.principal as UserDetailDto).user
		val staff = staffService.invite(invitation, user)
		return ResponseDto(payload = staff)
	}

	@PostMapping("${UrlPath.ACCEPT}/{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@Operation(summary = "Team Accept", security = [SecurityRequirement(name = "bearer-key")])
	fun accept(authentication: Authentication, @PathVariable id: Long): ResponseDto {
		val user = (authentication.principal as UserDetailDto).user
		val staff = staffService
		return ResponseDto(payload = staff)
	}

	@DeleteMapping("{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@Operation(summary = "Delete Staff", security = [SecurityRequirement(name = "bearer-key")])
	fun deleteStaff(
		@PathVariable id: Long,
	): ResponseDto {
		val message = staffService.inactive(id)
		return ResponseDto(message = translateService.toLocale(message), success = true)
	}
}