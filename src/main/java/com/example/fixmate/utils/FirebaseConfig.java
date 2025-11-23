package com.example.fixmate.utils;

import java.io.FileInputStream;
import java.io.InputStream;

import org.springframework.stereotype.Component;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.annotation.PostConstruct;

@Component
public class FirebaseConfig {

    @PostConstruct
    public void initFirebase() {
        try {
            InputStream serviceAccountStream;

            String secretPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");

            if (secretPath != null) {
                // Render server mode (secret file)
                System.out.println("🔐 Loading Firebase credentials from: " + secretPath);
                serviceAccountStream = new FileInputStream(secretPath);
            } else {
                // Local development (classpath)
                System.out.println("📁 Loading Firebase credentials from classpath");
                serviceAccountStream = getClass().getClassLoader()
                        .getResourceAsStream("panimithra-service-account.json");
            }

            if (serviceAccountStream == null) {
                throw new IllegalStateException("❌ Firebase credentials file not found!");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("🔥 Firebase initialized successfully!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
