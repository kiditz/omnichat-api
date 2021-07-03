package com.stafsus.waapi.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class QrCode(
	@Id
	@Column(nullable = false)
	var deviceId: String,
	@Column(nullable = false)
	var qrCode: String
) : Auditable()