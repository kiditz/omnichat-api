package com.stafsus.api.controller

import com.stafsus.api.constant.UrlPath
import com.stafsus.api.dto.ChannelDto
import com.stafsus.api.dto.ResponseDto
import com.stafsus.api.dto.ControlChannelDto
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
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping(UrlPath.CHANNEL)
@Tag(name = "Channel", description = "Channel API")
@Validated
class ChannelController(
	private val channelService: ChannelService
) {
	@PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
	@PreAuthorize("hasAuthority('ADMIN')")
	@Operation(summary = "Install Channel", security = [SecurityRequirement(name = "bearer-key")])
	fun install(
		@RequestParam name: String,
		@RequestParam productId: Long,
		@RequestParam(required = false) telegramToken: String?,
		@RequestParam(required = false) facebookToken: String?,
		@RequestParam(required = false) instagramToken: String?,
		@RequestParam(required = false) file: MultipartFile?,
		authentication: Authentication,
	): ResponseDto {
		val user = (authentication.principal as UserDetailDto).user
		val channelDto = ChannelDto(
			name = name,
			productId = productId,
			facebookToken = facebookToken,
			instagramToken = instagramToken,
			telegramToken = telegramToken,
			file = file,
		)
		return ResponseDto(payload = channelService.addChannel(channelDto, user))
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