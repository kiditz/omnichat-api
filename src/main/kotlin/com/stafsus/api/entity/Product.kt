package com.stafsus.api.entity

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import javax.persistence.*

@Entity
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator::class,
	property = "id"
)
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