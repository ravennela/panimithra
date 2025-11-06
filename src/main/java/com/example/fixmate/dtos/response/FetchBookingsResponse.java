package com.example.fixmate.dtos.response;

import java.time.LocalDate;

import com.example.fixmate.entities.Bookings;

public record FetchBookingsResponse(
        String bookingId,
        String name,
        String description,
        double amount,
        String paymentStatus,
        String bookingStatus,
        LocalDate bookingDate,
        String userId,
        String userName,
        String employeeId,
        String employeeName,
        String serviceName,
        String serviceId) {
    public static FetchBookingsResponse fromEntity(Bookings k) {
        return new FetchBookingsResponse(k.getId(), k.getName(),
                k.getDescription(), k.getTotalAmount(), k.getPaymentStatus(), k.getBookingStatus(),
                k.getBookingDate(), k.getCustomer().getId(),
                k.getCustomer().getName(), k.getEmployee().getId(), k.getEmployee().getName(), k.getService().getName(),
                k.getService().getId());
    }
}