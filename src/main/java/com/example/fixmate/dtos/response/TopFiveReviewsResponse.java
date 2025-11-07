package com.example.fixmate.dtos.response;

import com.example.fixmate.entities.Review;

public record TopFiveReviewsResponse(
        String reviewId,
        String serviceId,
        String bookingId,
        String comment,
        double rating,
        String userName,
        String employeeName,
        String userId,
        String employeeId,
        String serviceName

) {
    public static TopFiveReviewsResponse fromEntity(Review k) {
        return new TopFiveReviewsResponse(
                k.getId(), k.getService().getId(), k.getBookings().getId(),
                k.getComment(), k.getRating(), k.getCustomer().getName(), k.getEmployee().getName(),
                k.getCustomer().getId(), k.getEmployee().getId(), k.getService().getName());
    }
}
