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
	@GetMapping
	@Operation(
		security = [SecurityRequirement(name = "bearer-key")],
		summary = "Get devices by access token"
	)
	fun getDevices(principal: Principal): ResponseDto {
		val devices = waDeviceService.findDevices(principal.name, 0, 10)
		return ResponseDto.fromPage(devices)
	}

	@GetMapping("{id}")
	@Operation(
		security = [SecurityRequirement(name = "bearer-key")],
		summary = "Get devices by id"
	)
	fun findDevice(@PathVariable("id") deviceId: String): ResponseDto {
		val device = waDeviceService.findByDeviceId(deviceId)
		return ResponseDto(
			payload = device
		)
	}


	@PostMapping("/trial")
	@Operation(
		security = [SecurityRequirement(name = "bearer-key")],
		summary = "Start trial device"
	)
	fun startTrial(principal: Principal): ResponseDto {
		val device = waDeviceService.startTrial(principal)
		return ResponseDto(
			success = true,
			message = translateService.toLocale(MessageKey.UPDATE_DEVICE),
			payload = device
		)
	}

	@PostMapping("/install")
	@Operation(
		security = [SecurityRequirement(name = "bearer-key")],
		summary = "Install new device"
	)
	fun installDevice(principal: Principal): ResponseDto {
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
		val qrCode = waDeviceService.getQrCode(deviceRequest.deviceId)
		return ResponseDto(payload = qrCode, message = translateService.toLocale(MessageKey.QR_SUCCESS))
	}

	@Operation(security = [SecurityRequirement(name = "bearer-key")], summary = "Logout from whats app web")
	@DeleteMapping("/logout")
	fun logout(@RequestParam deviceId: String): ResponseDto {
		return waDeviceService.logout(deviceId)
	}
}