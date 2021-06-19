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
        val startAt: LocalDateTime? = null,
        val endAt: LocalDateTime? = null
) : Auditable()