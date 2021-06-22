package com.stafsus.waapi.service.dto

import com.fasterxml.jackson.annotation.JsonInclude

data class ResponseDto(
    var success: Boolean = true,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    var message: Any? = null,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    var payload: Any? = null
)