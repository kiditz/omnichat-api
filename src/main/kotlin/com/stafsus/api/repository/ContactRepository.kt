package com.stafsus.api.repository

import com.stafsus.api.entity.Contact
import org.springframework.data.jpa.repository.JpaRepository

interface ContactRepository : JpaRepository<Contact, Long> {
	fun findByNumberIn(numbers: List<String?>): List<Contact>
}