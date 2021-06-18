package com.stafsus.waapi.entity

import javax.persistence.*

@Entity
data class DeviceType(
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false)
    val name: String? = null,
    @Column(nullable = false)
    val timeToLeave: Long? = null,
) : Auditable()