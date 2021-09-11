package com.stafsus.api.event

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.stafsus.api.config.FirebaseProperties
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component

@Component
class FirebaseEvent(private val properties: FirebaseProperties) {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @EventListener(ApplicationReadyEvent::class)
    fun firebaseInitApp() {
        val serviceAccount = ClassPathResource("stafsus-35cc5-firebase-adminsdk-n5fih-7d862fce1a.json")
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount.inputStream))
            .setStorageBucket(properties.bucketName)
            .build()
        try {
            FirebaseApp.initializeApp(options)
            log.info("Firebase is ready!")
        }catch (ex: Exception){
            log.error("Firebase App already initialized!")
        }

    }
}