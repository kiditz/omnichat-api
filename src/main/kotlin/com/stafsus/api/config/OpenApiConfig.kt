package com.stafsus.api.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.parameters.Parameter
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.customizers.OperationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.HandlerMethod


@Configuration
class OpenApiConfig {
	@Bean
	fun customOpenAPI(): OpenAPI {
		return OpenAPI()
			.components(
				Components().addSecuritySchemes(
					"bearer-key",
					SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
				)
			).info(
				Info()
					.description("Layani API")
					.termsOfService("http://swagger.io/terms/")
					.license(
						License().name("Apache 2.0").url("http://springdoc.org")
					)
			)
	}

	@Bean
	fun customGlobalHeaders(): OperationCustomizer? {
		return OperationCustomizer { operation: Operation, _: HandlerMethod? ->
			operation.addParametersItem(
				Parameter()
					.`in`("header")
					.required(false)
					.description("Language")
					.name("Accept-Language")

			)
			operation.addParametersItem(
				Parameter()
					.`in`("header")
					.required(false)
					.description("Company Id")
					.name("X-CompanyID")

			)
		}
	}
}