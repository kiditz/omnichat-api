package com.stafsus.waapi.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.stafsus.waapi.service.security.JwtProvider
import com.stafsus.waapi.service.security.SecurityUserService
import com.stafsus.waapi.service.security.CustomAccessDeniedHandler
import com.stafsus.waapi.service.security.JwtAuthenticationEntryPoint
import com.stafsus.waapi.service.security.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.BeanIds
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    val jwtProvider: JwtProvider,
    val userService: SecurityUserService,
    val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
    val objectMapper: ObjectMapper
) : WebSecurityConfigurerAdapter() {
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Bean
    fun jwtAuthenticationFilter(): JwtAuthenticationFilter? {
        return JwtAuthenticationFilter(jwtProvider = jwtProvider, securityUserService = userService)
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!.userDetailsService(userService).passwordEncoder(passwordEncoder())
    }

    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers(
            "/v3/api-docs/**",
            "'/favicon.ico",
            "/swagger-ui.html",
            "/swagger-ui/**"
        )
    }

    @Bean
    fun accessDeniedHandler(): AccessDeniedHandler {
        return CustomAccessDeniedHandler(objectMapper)
    }

    override fun configure(http: HttpSecurity?) {
        http!!.antMatcher("/api/**")
            .cors().and()
            .csrf().disable()
            .logout().disable()
            .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(accessDeniedHandler())
            .and()
            .headers()
            .frameOptions().sameOrigin()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/api/auth/refresh-token","/api/auth/sign-in", "/api/auth/sign-up", "/api/auth/sign-out", "/api/health").permitAll()
            .antMatchers("/api/device/logout").permitAll()
            .anyRequest().authenticated()
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }
}