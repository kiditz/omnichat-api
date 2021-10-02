package com.stafsus.api.entity

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import javax.persistence.*

@Entity
@Table(name = "user_principal")
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator::class,
	property = "id"
)
data class UserPrincipal(
	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
	@Column(nullable = false, length = 60)
	var email: String,
	@Column(nullable = false, length = 100)
	var name: String,
	@Column(nullable = false, length = 60)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	var password: String,
	@Column(nullable = false)
	var imageUrl: String? = null,
	@Column(nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	var status: Status,
//	@ElementCollection(fetch = FetchType.EAGER, targetClass = Authority::class)
//	@CollectionTable(name = "user_authority", joinColumns = [JoinColumn(name = "user_id")])
//	@Column(name = "authority")
//	var authorities: Set<Authority>,
	@Column(nullable = false)
	var isVerified: Boolean,
	@OneToOne(cascade = [CascadeType.ALL])
	@JoinColumn(name = "quota_id", nullable = true)
//	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	var quota: Quota? = null,

	) : Auditable()
