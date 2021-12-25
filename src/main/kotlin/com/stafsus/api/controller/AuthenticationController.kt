package com.stafsus.api.controller

import com.stafsus.api.constant.UrlPath
import com.stafsus.api.dto.*
import com.stafsus.api.service.AuthenticationService
import com.stafsus.api.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping(UrlPath.API_AUTH)
@Tag(name = "Authentication", description = "Authentication for admin application")
@Validated
class AuthenticationController(
	private val authenticationService: AuthenticationService,
	private val userService: UserService
) {
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

	@GetMapping(UrlPath.AUTHORITY)
	@Operation(summary = "Get User Authorities")
	fun getAuthorities(): ResponseDto {
		return ResponseDto(payload = authenticationService.getAuthorities())
	}

	@PostMapping(UrlPath.SIGN_UP)
	@Operation(summary = "Register user as admin")
	fun registerAdmin(@Valid @RequestBody request: SignUpDto): ResponseDto {
		val user = userService.signUp(request)
		return ResponseDto(payload = user)
	}
}