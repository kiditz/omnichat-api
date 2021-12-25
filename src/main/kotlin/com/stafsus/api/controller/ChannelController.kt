package com.stafsus.api.controller

import com.stafsus.api.constant.UrlPath
import com.stafsus.api.dto.ChannelDto
import com.stafsus.api.dto.ControlChannelDto
import com.stafsus.api.dto.ResponseDto
import com.stafsus.api.dto.UserDetailDto
import com.stafsus.api.service.ChannelService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
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
	@PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
	@PreAuthorize("hasAuthority('ADMIN')")
	@Operation(summary = "Add new channel", security = [SecurityRequirement(name = "bearer-key")])
	fun addChannel(
		@ModelAttribute channelDto: ChannelDto,
	): ResponseDto {
		return ResponseDto(payload = channelService.addChannel(channelDto))
	}

	@PostMapping("/{id}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
	@PreAuthorize("hasAuthority('ADMIN')")
	@Operation(summary = "Edit existing channel", security = [SecurityRequirement(name = "bearer-key")])
	fun editChannel(
		@PathVariable id: Long,
		@ModelAttribute channelDto: ChannelDto,
	): ResponseDto {
		return ResponseDto(payload = channelService.editChannel(id, channelDto))
	}

	@PostMapping("/control")
	@PreAuthorize("hasAuthority('ADMIN')")
	@Operation(summary = "Enable/Disable Channel", security = [SecurityRequirement(name = "bearer-key")])
	fun control(@RequestBody channelDto: ControlChannelDto, authentication: Authentication): ResponseDto {
		val user = (authentication.principal as UserDetailDto).user
		val channel = channelService.controlChannel(channelDto, user)
		return ResponseDto(payload = channel)
	}

	@GetMapping
	@Operation(summary = "Get Active Channels", security = [SecurityRequirement(name = "bearer-key")])
	fun findChannel(
		@RequestParam productId: Long,
		@RequestParam page: Int,
		@RequestParam size: Int,
	): ResponseDto {
		val channels = channelService.findChannels(productId, page, size)
		return ResponseDto.fromPage(channels)
	}
}