package com.stafsus.api.config

import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Order(2)
@Configuration
class WebSecurityConfiguration : WebSecurityConfigurerAdapter() {
	override fun configure(http: HttpSecurity) {
		http.exceptionHandling().accessDeniedPage("/403")
		http
			.antMatcher("/**")
			.csrf()
			.and() //                .csrf().disable()
			.authorizeRequests()
			.antMatchers(
				"/**/favicon.ico",
				"/**/*.css",
				"/**/*.js",
				"/**/*.png",
				"/**/*.xlsx",
				"/fonts/**",
				"/logout"
			).permitAll()
			.antMatchers(
				"/v3/api-docs/**",
				"'/favicon.ico",
				"/swagger-ui.html",
				"/swagger-ui/**"
			).permitAll()
			.and()
			.formLogin()
			.loginPage("/login")
			.permitAll()
			.defaultSuccessUrl("/", true)
			.and()
			.logout()
			.logoutSuccessUrl("/login?logout")
	}

	@Throws(java.lang.Exception::class)
	override fun configure(web: WebSecurity) {
		web
			.ignoring()
			.antMatchers(
				HttpMethod.GET,
				"/",
				"/*.html",
				"/**/favicon.ico",
				"/**/*.html",
				"/**/*.css",
				"/**/*.js",
				"/**/*.xlsx"
			)
	}
}