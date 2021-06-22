package com.stafsus.waapi.api

import com.stafsus.waapi.api.model.DeviceRequest
import com.stafsus.waapi.constant.MessageKey
import com.stafsus.waapi.service.TranslateService
import com.stafsus.waapi.service.WaDeviceService
import com.stafsus.waapi.service.dto.ResponseDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.security.Principal
import javax.validation.Valid

@RestController
@RequestMapping("/api/device")
@Validated
@Tag(name = "Device", description = "Control your whats app web device")
class DeviceController(
        private val waDeviceService: WaDeviceService,
        private val translateService: TranslateService
) {
    @PostMapping("/install")
    @Operation(
            security = [SecurityRequirement(name = "bearer-key")],
            summary = "Install new device"
    )
    fun installDevice(@Valid @RequestBody deviceRequest: DeviceRequest, principal: Principal): ResponseDto {
        val device = waDeviceService.install(principal)
        return ResponseDto(
                success = true,
                message = translateService.toLocale(MessageKey.UPDATE_DEVICE),
                payload = device
        )
    }

    @PostMapping("/restart")
    @Operation(
            security = [SecurityRequirement(name = "bearer-key")],
            summary = "Restart existing device"
    )
    fun restartDevice(@Valid @RequestBody deviceRequest: DeviceRequest, principal: Principal): ResponseDto {
        waDeviceService.restart(deviceRequest.deviceId, principal.name)
        return ResponseDto(
                success = true,
                message = translateService.toLocale(MessageKey.UPDATE_DEVICE),
        )
    }

    @PostMapping("/uninstall")
    @Operation(
            security = [SecurityRequirement(name = "bearer-key")],
            summary = "Uninstall device"
    )
    fun uninstallDevice(@Valid @RequestBody deviceRequest: DeviceRequest, principal: Principal): ResponseDto {
        waDeviceService.uninstall(deviceRequest.deviceId, principal.name)
        return ResponseDto(message = translateService.toLocale(MessageKey.UNINSTALL_DEVICE))
    }

    @PostMapping("/qr")
    @Operation(
            security = [SecurityRequirement(name = "bearer-key")],
            summary = "Get qrcode by client"
    )
    fun getQrCode(@Valid @RequestBody deviceRequest: DeviceRequest, principal: Principal): ResponseDto {
        return waDeviceService.getQrCode(deviceRequest.deviceId)
    }
}