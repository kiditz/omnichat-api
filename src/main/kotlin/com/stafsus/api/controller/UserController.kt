package com.stafsus.api.controller

import com.stafsus.api.constant.UrlPath
import com.stafsus.api.dto.EditUserDto
import com.stafsus.api.dto.ResponseDto
import com.stafsus.api.dto.UserDetailDto
import com.stafsus.api.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid

@RestController
@RequestMapping(UrlPath.API_USER)
@Tag(name = "User", description = "User Management API")
@Validated
class UserController(
	private val userService: UserService
) {
	@Operation(security = [SecurityRequirement(name = "bearer-key")], summary = "Get User By jwt token")
	@GetMapping
	fun getUser(authentication: Authentication): ResponseDto {
		val userPrincipal = authentication.principal as UserDetailDto
		return ResponseDto(payload = userPrincipal.user)
	}


	@PutMapping
	@Operation(security = [SecurityRequirement(name = "bearer-key")], summary = "Update user details")
	fun editUser(@Valid @RequestBody request: EditUserDto, authentication: Authentication): ResponseDto {
		val userDetail = authentication.principal as UserDetailDto
		val userPrincipal = userService.editUser(request, userDetail.user)
		return ResponseDto(payload = userPrincipal)
	}

	@PutMapping(UrlPath.PHOTO, consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
	@Operation(security = [SecurityRequirement(name = "bearer-key")], summary = "Update user details")
	fun updatePhoto(
		authentication: Authentication,
		@RequestParam file: MultipartFile,
	): ResponseDto {
		val userDetail = authentication.principal as UserDetailDto
		return ResponseDto(payload = userService.updateImage(userDetail.user, file))
	}
}