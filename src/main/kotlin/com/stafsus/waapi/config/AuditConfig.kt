package com.stafsus.waapi.config

import com.stafsus.waapi.config.audit.AuditAwareImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@Configuration
@EnableJpaAuditing
class AuditConfig {
    @Bean
    fun auditorProvider(): AuditorAware<String> {
        return AuditAwareImpl()
    }
}