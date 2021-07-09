package com.stafsus.waapi.service.dto

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.data.domain.Page

data class ResponseDto(
	var success: Boolean = true,
	@JsonInclude(JsonInclude.Include.NON_NULL)
	var message: Any? = null,
	@JsonInclude(JsonInclude.Include.NON_NULL)
	var payload: Any? = null,
	@JsonInclude(JsonInclude.Include.NON_NULL)
	var errors: Any? = null,
	@JsonInclude(JsonInclude.Include.NON_NULL)
	var totalPage: Int? = null,
	@JsonInclude(JsonInclude.Include.NON_NULL)
	var size: Int? = null,
	@JsonInclude(JsonInclude.Include.NON_NULL)
	var numberOfElement: Int? = null,

	) {
	companion object {
		fun <T> fromPage(page: Page<T>): ResponseDto {
			return ResponseDto(
				payload = page.content,
				totalPage = page.totalPages,
				size = page.size,
				numberOfElement = page.numberOfElements
			)
		}
	}
}