package com.stafsus.waapi.api

import com.stafsus.waapi.api.model.DeviceRequest
import com.stafsus.waapi.constant.MessageKey
import com.stafsus.waapi.service.TranslateService
import com.stafsus.waapi.service.WaDeviceService
import com.stafsus.waapi.service.dto.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.security.Principal
import javax.validation.Valid

@RestController
@RequestMapping("/api/device")
@Validated
class DeviceController(
        private val waDeviceService: WaDeviceService,
        private val translateService: TranslateService
) {
    @PostMapping("/update")
    @Operation(
            security = [SecurityRequirement(name = "bearer-key")],
            summary = "Restart existing device"
    )
    fun restartDevice(@Valid @RequestBody deviceRequest: DeviceRequest, principal: Principal): ApiResponse {
        waDeviceService.restart(deviceRequest.deviceId, principal.name)
        return ApiResponse(success = true, message = translateService.toLocale(MessageKey.UPDATE_DEVICE))
    }

    @PostMapping("/uninstall")
    @Operation(
            security = [SecurityRequirement(name = "bearer-key")],
            summary = "Register new user account"
    )
    fun uninstallDevice(@Valid @RequestBody deviceRequest: DeviceRequest, principal: Principal): ApiResponse {
        waDeviceService.uninstall(deviceRequest.deviceId, principal.name)
        return ApiResponse(message = translateService.toLocale(MessageKey.UNINSTALL_DEVICE))
    }
}