package com.stafsus.api.entity

import javax.persistence.*

@Entity
@Table
data class Product(
	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
	@Column(nullable = false, length = 60)
	var name: String,
	@Column(name = "image_url", nullable = false)
	var imageUrl: String,
	@Column(name = "description_en")
	var descriptionEn: String? = null,
	@Column(name = "description_id")
	var descriptionId: String? = null,
	@Column(nullable = false)
	var priority: Long,
	@Enumerated(EnumType.STRING)
	var type: ProductType? = null,
) : Auditable()