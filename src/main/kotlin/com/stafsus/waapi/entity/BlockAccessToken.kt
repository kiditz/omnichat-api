package com.stafsus.waapi.entity

import javax.persistence.*

@Entity
data class BlockAccessToken(
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false, columnDefinition = "text")
    var token: String,
) : Auditable()