package com.stafsus.waapi.entity

import com.fasterxml.jackson.annotation.JsonValue
import com.stafsus.waapi.constant.Display
import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class WaDevice(
	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
	@Column(nullable = false, columnDefinition = "varchar(12) default ''")
	var deviceId: String,
	var phone: String? = null,
	@Column(nullable = false, columnDefinition = "boolean default false")
	var isTrial: Boolean? = null,
	@ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	var user: User? = null,
	var startAt: LocalDateTime? = null,
	var endAt: LocalDateTime? = null,
	@Enumerated(EnumType.STRING)
	var deviceStatus: DeviceStatus? = null,
	@Enumerated(EnumType.STRING)
	var deviceInfo: DeviceInfo? = null,
	@Column(columnDefinition = "text")
	var session: String? = null
) : Auditable()

enum class DeviceStatus(@JsonValue val display: String) {
	PHONE_ONLINE(Display.PHONE_ONLINE), PHONE_OFFLINE(Display.PHONE_OFFLINE);
}

enum class DeviceInfo(@JsonValue val display: String) {
	ON_INSTALL(Display.DEVICE_ON_INSTALL),
	ON_UNINSTALLED(Display.DEVICE_ON_UNINSTALL),
	ON_RESTART(Display.DEVICE_ON_RESTART),
	ACTIVE(Display.DEVICE_ON_ACTIVE),
	INACTIVE(Display.DEVICE_ON_INACTIVE);
}