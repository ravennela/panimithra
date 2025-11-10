package com.example.fixmate.dtos.response;

import java.time.LocalDateTime;

import com.example.fixmate.entities.User;

public record UserProfileResponse(
        String userId,
        String fullName,
        String email,
        String phoneNumber,
        String profileImage,
        LocalDateTime joinDate,
        String role) {
    public static UserProfileResponse fromEntity(User k) {
        return new UserProfileResponse(k.getId(), k.getName(), k.getEmailId(), k.getContactNumber(),
                k.getProfileImageUrl(), k.getCreatedAt(), k.getRole());
    }
}
