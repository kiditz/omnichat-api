package com.stafsus.waapi.entity

import javax.persistence.*

@Entity
data class Setting(
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @Column(nullable = false)
    val apiKey: String,
    @Column(nullable = false)
    val limitPhoneNumber: Int,
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id")
    var user: User? = null,
) : Auditable()