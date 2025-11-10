package com.example.fixmate.dtos.response;

import com.example.fixmate.entities.SubscriptionPlan;

public record FetchPlansReponse(
        String planId,
        String planName,
        String planDescription,
        double price,
        int duration,
        String status,
        String discount,
        double originalPrice) {
    public static FetchPlansReponse fromEntity(SubscriptionPlan k) {
        return new FetchPlansReponse(k.getId(), k.getPlanName(), k.getDescription(), k.getPrice(),
                k.getDurationInDays(), k.getStatus(), k.getDiscount(), k.getOriginalPrice());

    }
}
