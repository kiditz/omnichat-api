package com.stafsus.api.controller

import com.stafsus.api.constant.UrlPath
import com.stafsus.api.dto.ChannelDto
import com.stafsus.api.dto.ResponseDto
import com.stafsus.api.dto.RestartChannelDto
import com.stafsus.api.dto.UserDetailDto
import com.stafsus.api.service.ChannelService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(UrlPath.CHANNEL)
@Tag(name = "Channel", description = "Channel API")
@Validated
class ChannelController(
	private val channelService: ChannelService
) {
	@PostMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	@Operation(summary = "Install Channel", security = [SecurityRequirement(name = "bearer-key")])
	fun install(@RequestBody channelDto: ChannelDto, authentication: Authentication): ResponseDto {
		val user = (authentication.principal as UserDetailDto).user
		val channel = channelService.install(channelDto, user)
		return ResponseDto(payload = channel)
	}

	@PostMapping("/restart")
	@PreAuthorize("hasAuthority('ADMIN')")
	@Operation(summary = "Restart Channel", security = [SecurityRequirement(name = "bearer-key")])
	fun restart(@RequestBody channelDto: RestartChannelDto, authentication: Authentication): ResponseDto {
		val user = (authentication.principal as UserDetailDto).user
		val channel = channelService.restart(channelDto.deviceId!!, user)
		return ResponseDto(payload = channel)
	}

	@GetMapping
	@Operation(summary = "Get Active Channels", security = [SecurityRequirement(name = "bearer-key")])
	fun findChannel(
		@RequestParam page: Int,
		@RequestParam size: Int,
	): ResponseDto {
		val channels = channelService.findChannels(page, size)
		return ResponseDto.fromPage(channels)
	}
}