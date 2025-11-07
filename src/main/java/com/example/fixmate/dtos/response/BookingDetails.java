package com.example.fixmate.dtos.response;

import java.time.LocalDate;

import com.example.fixmate.entities.Bookings;

public record BookingDetails(
        String bookingId,
        String serviceId,
        String customerId,
        String employeeId,
        String bookingStatus,
        String serviceName,
        String providerName,
        LocalDate bookDate,
        double price,
        String category,
        String employeeContact,
        String serviceDescription,
        String paymentStatus) {
    public static BookingDetails fromEntity(Bookings k) {
        return new BookingDetails(k.getId(), k.getService().getId(), k.getCustomer().getId(), k.getEmployee().getId(),
                k.getBookingStatus(), k.getService().getName(), k.getEmployee().getName(), k.getBookingDate(),
                k.getService().getPrice(), k.getService().getCategory().getCategoryName(),
                k.getEmployee().getContactNumber(), k.getService().getDescription(), k.getPaymentStatus());
    }

}
