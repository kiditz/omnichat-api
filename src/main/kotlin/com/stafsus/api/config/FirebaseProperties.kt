package com.stafsus.api.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "firebase")
class FirebaseProperties {
    lateinit var bucketName: String
    lateinit var imageUrl: String
    lateinit var root: String
}