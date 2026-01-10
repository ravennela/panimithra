package com.example.fixmate.utils;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initializeFirebase() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.getApplicationDefault())
                        .build();

                FirebaseApp.initializeApp(options);
                System.out.println("🔥 Firebase initialized using ADC");
            }
        } catch (Exception e) {
            throw new IllegalStateException("❌ Firebase initialization failed", e);
        }
    }
}
