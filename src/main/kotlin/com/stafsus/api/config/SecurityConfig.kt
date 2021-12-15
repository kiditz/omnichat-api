package com.stafsus.api.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.stafsus.api.service.JwtService
import com.stafsus.api.service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.BeanIds
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Order(1)
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfig(
	private val passwordEncoder: PasswordEncoder,
	private val objectMapper: ObjectMapper,
	private val userService: UserService,
	private val jwtService: JwtService,
	private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint
) : WebSecurityConfigurerAdapter() {


	private fun accessDeniedHandler(): AccessDeniedHandler {
		return CustomAccessDeniedHandler(objectMapper)
	}

	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	override fun authenticationManagerBean(): AuthenticationManager {
		return super.authenticationManagerBean()
	}

	override fun configure(auth: AuthenticationManagerBuilder) {
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder)
	}

	private fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
		return JwtAuthenticationFilter(userService, jwtService)
	}

	@Throws(Exception::class)
	override fun configure(http: HttpSecurity) {
		http
			.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
			.antMatcher("/api/**")
			.cors().and()
			.csrf().disable()
			.logout().disable()
			.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
			.accessDeniedHandler(accessDeniedHandler())
			.and()
			.headers()
			.frameOptions().sameOrigin()
			.and()
			.authorizeRequests()
			.antMatchers(
				"/api/auth/refresh-token",
				"/api/auth/sign-in",
				"/api/auth/sign-up",
				"/api/staff/sign-up",
				"/api/staff/check-staff",
				"/api/auth/sign-out",
				"/api/industry",
				"/api/auth/authority"
			).permitAll()
			.anyRequest().authenticated()
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	}
}
