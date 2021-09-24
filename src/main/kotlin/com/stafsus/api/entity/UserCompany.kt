package com.stafsus.api.entity

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import javax.persistence.*

@Entity
@Table
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator::class,
	property = "userCompanyId"
)
data class UserCompany(
	@EmbeddedId
	val id: UserCompanyId? = null,
	@ManyToOne
	@MapsId("userPrincipalId")
	var userPrincipal: UserPrincipal? = null,
	@ManyToOne
	@MapsId("companyId")
	var company: Company? = null,
	@ManyToOne
	@MapsId("userAuthorityId")
	var userAuthority: UserAuthority? = null
) : Auditable()