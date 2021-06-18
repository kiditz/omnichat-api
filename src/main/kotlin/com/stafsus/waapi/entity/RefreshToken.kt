package com.stafsus.waapi.entity

import javax.persistence.*

@Entity
data class RefreshToken(
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false)
    val token: String,
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id")
    var user: User? = null,
) : Auditable()