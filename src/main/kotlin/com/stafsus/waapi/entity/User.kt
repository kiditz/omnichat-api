package com.stafsus.waapi.entity

import javax.persistence.*

@Entity
@Table(name = "user_principal")
data class User(
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false)
    var email: String,
    @Column(nullable = false)
    val username: String,
    @Column(nullable = false)
    var password: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var role: Role,

    @ElementCollection(fetch = FetchType.EAGER, targetClass = Authority::class)
    @CollectionTable(name = "user_authority", joinColumns = [JoinColumn(name = "user_id")])
    @Column(name = "authority")
    var authorities: List<Authority>,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: Status,

    @OneToMany(
        mappedBy = "user", fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL]
    )
    var devices: Set<WaDevice>? = null
) : Auditable()
