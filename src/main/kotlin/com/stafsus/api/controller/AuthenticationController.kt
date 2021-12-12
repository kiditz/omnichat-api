package com.stafsus.api.controller

import com.stafsus.api.constant.UrlPath
import com.stafsus.api.dto.*
import com.stafsus.api.service.AuthenticationService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping(UrlPath.API_AUTH)
@Tag(name = "Authentication", description = "Authentication for admin application")
@Validated
class AuthenticationController(
	private val authenticationService: AuthenticationService
) {
	private val log = LoggerFactory.getLogger(javaClass)

	@PostMapping(UrlPath.SIGN_IN)
	@Operation(summary = "Get access token")
	fun signIn(@Valid @RequestBody request: SignInDto): ResponseDto {
		val accessToken = authenticationService.signIn(request.email!!, request.password!!)
		return ResponseDto(payload = accessToken)
	}


	@PostMapping(UrlPath.REFRESH)
	@Operation(summary = "Renew access token by refresh token")
	fun refresh(@Valid @RequestBody request: RefreshTokenDto): ResponseDto {
		val accessToken = authenticationService.refresh(request.refresh!!)
		return ResponseDto(payload = accessToken)
	}
}