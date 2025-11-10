package com.example.fixmate.dtos.response;

import com.example.fixmate.entities.Review;

public record ReviewResponse(
        String serviceId,
        String bookingId,
        String customerId,
        String employeeId,
        String customerName,
        String employeeName,
        String comment,
        double rating) {
    public static ReviewResponse fromEntity(Review k) {
        return new ReviewResponse(k.getService().getId(), k.getBookings().getId(), k.getCustomer().getId(),
                k.getEmployee().getId(), k.getCustomer().getName(), k.getEmployee().getName(), k.getComment(),
                k.getRating());
    }
}
