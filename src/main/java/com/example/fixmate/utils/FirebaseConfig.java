package com.example.fixmate.utils;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        try {
            GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .setProjectId("panimithra-e575e") // 🔴 REQUIRED
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("🔥 Firebase initialized using ADC");
            }
            return FirebaseApp.getInstance();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("❌ Firebase initialization failed", e);
        }
    }
}
