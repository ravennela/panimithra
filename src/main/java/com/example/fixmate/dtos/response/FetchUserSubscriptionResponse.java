package com.example.fixmate.dtos.response;

import java.time.LocalDate;

import com.example.fixmate.entities.Subscription;

public record FetchUserSubscriptionResponse(
                String planname,
                double price,
                String id,
                String status,
                LocalDate startDate,
                LocalDate enDate) {
        public static FetchUserSubscriptionResponse fromEntity(Subscription k) {
                return new FetchUserSubscriptionResponse(k.getSubscriptionPlan().getPlanName(),
                                k.getSubscriptionPlan().getPrice(), k.getId(), k.getStatus(), k.getStartDate(),
                                k.getEndDate());
        }

}
