package com.example.fixmate.dtos.response;

import java.time.LocalDate;

import com.example.fixmate.entities.User;

public record FetchUserResponse(
        String userId,
        String userName,
        String status,
        String role,
        String email,
        String contactNumber,
        String profileImageUrl,
        String gender,
        LocalDate dob,
        String city,
        String state,
        String pinCode,
        String primaryService,
        int experiance,
        String shortBio,
        double latitude,
        double longitude
        ) {

    public static FetchUserResponse fromEntity(User k) {

        return new FetchUserResponse(k.getId(), k.getName(), k.getStatus(), k.getRole(), k.getEmailId(),
                k.getContactNumber(), k.getProfileImageUrl(), k.getGender(), k.getDateOfBirth(), k.getCity(),
                k.getState(), k.getPincode(), k.getPrimaryService(), k.getExperiance(), k.getShortBio(), k.getLatitude(), k.getLongitude());

    }
}
