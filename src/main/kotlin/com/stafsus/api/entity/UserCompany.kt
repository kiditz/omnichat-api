package com.stafsus.api.entity

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import org.hibernate.Hibernate
import java.util.*
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
) : Auditable() {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
		other as UserCompany

		return id != null && id == other.id
	}

	override fun hashCode(): Int = Objects.hash(id);

	@Override
	override fun toString(): String {
		return this::class.simpleName + "(EmbeddedId = $id )"
	}
}