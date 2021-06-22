package com.stafsus.waapi.api

import com.stafsus.waapi.api.model.RefreshTokenRequest
import com.stafsus.waapi.api.model.SignInRequest
import com.stafsus.waapi.api.model.SignUpRequest
import com.stafsus.waapi.constant.MessageKey
import com.stafsus.waapi.service.TranslateService
import com.stafsus.waapi.service.dto.ResponseDto
import com.stafsus.waapi.service.security.AuthenticationService
import com.stafsus.waapi.service.security.JwtAuthenticationFilter
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

import javax.validation.Valid

@RestController
@RequestMapping("/api")
@Tag(name = "Authentication", description = "Used to authenticated user")
@Validated
class AuthenticationController(
        private val authenticationService: AuthenticationService,
        private val translateService: TranslateService

) {

    @PostMapping("/auth/sign-up")
    @Operation(summary = "Register new user account")
    fun signUp(@Valid @RequestBody request: SignUpRequest): ResponseDto {
        val userDto = authenticationService.signUp(request.toDto())
        return ResponseDto(payload = userDto)
    }

    @PostMapping("/auth/sign-in")
    @Operation(summary = "Get jwt token by sign in")
    fun signIn(@Valid @RequestBody request: SignInRequest): ResponseDto {
        val userDto = authenticationService.signIn(request.toDto())
        return ResponseDto(payload = userDto)
    }

    @PostMapping("/auth/refresh-token")
    @Operation(summary = "Get jwt token by refresh token")
    fun refresh(@Valid @RequestBody request: RefreshTokenRequest): ResponseDto {
        val userDto = authenticationService.refreshToken(request.refreshToken!!)
        return ResponseDto(payload = userDto)
    }

    @PostMapping("/auth/sign-out")
    @Operation(
            security = [SecurityRequirement(name = "bearer-key")],
            summary = "Sign out jwt token"
    )
    fun signOut(@Valid @RequestBody request: RefreshTokenRequest, sRequest: HttpServletRequest): ResponseDto {
        val accessToken = JwtAuthenticationFilter.getJwtFromRequest(sRequest)
        authenticationService.signOut(request.refreshToken!!, accessToken!!)
        return ResponseDto(message = translateService.toLocale(MessageKey.SIGN_OUT_SUCCESS))
    }


}
