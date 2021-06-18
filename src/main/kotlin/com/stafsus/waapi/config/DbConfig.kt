package com.stafsus.waapi.config

import com.stafsus.waapi.entity.User
import com.stafsus.waapi.repository.UserRepository
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EntityScan(basePackageClasses = [User::class])
@EnableJpaRepositories(basePackageClasses = [UserRepository::class])
@EnableTransactionManagement
class DbConfig