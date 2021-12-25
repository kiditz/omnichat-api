package com.stafsus.api.entity

import java.io.Serializable
import javax.persistence.Embeddable

@Embeddable
data class UserCompanyId(
	val userPrincipalId: Long? = null,
	val companyId: Long? = null,
	val userAuthorityId: Long? = null,
) : Serializable