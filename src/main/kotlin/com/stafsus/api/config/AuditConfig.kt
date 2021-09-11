package com.stafsus.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
/**
 * Handle jpa versioning to fill createdBy, updateAt and updatedBy
 * */
@Configuration
@EnableJpaAuditing
class AuditConfig {
    @Bean
    fun auditorProvider(): AuditorAware<String> {
        return AuditAwareImpl()
    }
}