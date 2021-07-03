package com.stafsus.waapi.api.model

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class DeviceRequest(
        @field:NotBlank
        @field:Size(min = 8, max = 10)
        val deviceId: String,
)