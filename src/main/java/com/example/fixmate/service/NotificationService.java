package com.example.fixmate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Profile; // ✅ THIS WAS MISSING

import com.example.fixmate.dtos.response.CreateSubcategoryResponse;
import com.example.fixmate.entities.User;
import com.example.fixmate.repositories.UserRepository;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
@Service
public class NotificationService {

    @Autowired
    UserRepository userRepository;

    public String sendNotification(String token, String title, String body)
            throws FirebaseMessagingException {

        // Firebase not configured → skip safely
        if (FirebaseApp.getApps().isEmpty()) {
            return "Firebase disabled";
        }

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .putData("click_action", "FLUTTER_NOTIFICATION_CLICK")
                .build();

        return FirebaseMessaging.getInstance().send(message);
    }

    public CreateSubcategoryResponse registerToken(String token, String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("No User Found");
        }

        user.setDeviceToken(token);
        userRepository.save(user);

        CreateSubcategoryResponse response = new CreateSubcategoryResponse();
        response.setId(userId);
        response.setMessage("Token Saved Successfully");
        return response;
    }
}
