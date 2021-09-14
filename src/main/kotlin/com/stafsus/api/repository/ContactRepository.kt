package com.stafsus.api.repository

import com.stafsus.api.entity.Contact
import org.springframework.data.jpa.repository.JpaRepository

interface ContactRepository : JpaRepository<Contact, String> {
	fun findByIdIn(ids: List<String?>): List<Contact>
}