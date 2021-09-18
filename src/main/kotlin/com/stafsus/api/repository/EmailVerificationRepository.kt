package com.stafsus.api.repository

import com.stafsus.api.entity.EmailVerification
import org.springframework.data.jpa.repository.JpaRepository

interface EmailVerificationRepository : JpaRepository<EmailVerification, Long> {
}