package com.stafsus.api.controller

import com.stafsus.api.constant.UrlPath
import com.stafsus.api.dto.ResponseDto
import com.stafsus.api.dto.SignUpDto
import com.stafsus.api.dto.StaffSignUpDto
import com.stafsus.api.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping
@Tag(name = "Registration", description = "Register new user with credentials")
@Validated
class RegistrationController(
	private val userService: UserService
) {

	@PostMapping(UrlPath.ADMIN_SIGN_UP)
	@Operation(summary = "Register user as admin")
	fun registerAdmin(@Valid @RequestBody request: SignUpDto): ResponseDto {
		val user = userService.signUp(request)
		return ResponseDto(payload = user)
	}
	@PostMapping(UrlPath.STAFF_SIGN_UP)
	@Operation(summary = "Register user with invitation code")
	fun registerStaff(@Valid @RequestBody request: StaffSignUpDto): ResponseDto {
		val user = userService.invitationSignUp(request)
		return ResponseDto(payload = user)
	}
}