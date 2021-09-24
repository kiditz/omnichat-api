package com.stafsus.api.repository

import com.stafsus.api.entity.Staff
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface StaffRepository : JpaRepository<Staff, Long> {
}