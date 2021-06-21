package com.stafsus.waapi.entity

import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class WaDevice(
        @Id
        @Column(nullable = false)
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        @Column(nullable = false)
        var deviceId: String,
        var phone: String? = null,
        @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        var user: User? = null,
        var startAt: LocalDateTime? = null,
        var endAt: LocalDateTime? = null,
        @Enumerated(EnumType.STRING)
        var deviceStatus: DeviceStatus? = null,
        @Enumerated(EnumType.STRING)
        var deviceInfo: DeviceInfo? = null
) : Auditable()

enum class DeviceStatus {
    PHONE_ONLINE, PHONE_OFFLINE
}

enum class DeviceInfo {
    ON_INSTALL, ON_UNINSTALLED, ON_RESTART, ACTIVE, INACTIVE
}