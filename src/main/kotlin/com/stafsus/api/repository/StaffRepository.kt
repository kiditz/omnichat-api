package com.stafsus.api.repository

import com.stafsus.api.entity.Staff
import org.springframework.data.jpa.repository.JpaRepository

interface StaffRepository : JpaRepository<Staff, Long> {
}