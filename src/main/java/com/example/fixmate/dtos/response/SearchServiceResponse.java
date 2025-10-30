package com.example.fixmate.dtos.response;

import java.util.List;

import com.example.fixmate.entities.Bookings;
import com.example.fixmate.entities.Review;
import com.example.fixmate.entities.ServiceEntity;

public record SearchServiceResponse(
        String serviceName,
        String description,
        String status,
        String address,
        double latitude,
        double longitude,
        int duration,
        String serviceId,
        List<Bookings> bookings,
        String categoryName,
        String categoryDescription,
        String subCategoryName,
        String subCategoroyDescription,
        double avgrating,
        double price,
        String employeeName,
        String employeeId) {
    public static SearchServiceResponse fromEntity(ServiceEntity k) {
        String employeeName = k.getEmployee() != null ? k.getEmployee().getName() : null;
        String employeeId = k.getEmployee() != null ? k.getEmployee().getId() : null;
        return new SearchServiceResponse(k.getName(), k.getDescription(), k.getStatus(), k.getAddress(),
                k.getLatitude(), k.getLongitude(), k.getDuration(), k.getId(),
                k.getBookings(), k.getCategory().getCategoryName(), k.getCategory().getDescription(),
                k.getSubCategory().getSubCategoryName(), k.getSubCategory().getDescription(),
                calculateAverageRating(k.getReviews()), k.getPrice(), employeeName,
                employeeId);

    }

    public static double calculateAverageRating(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty())
            return 0.0;
        return reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);
    }
}
