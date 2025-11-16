package com.example.fixmate.dtos.response;

import java.time.LocalDateTime;

import com.example.fixmate.entities.ServiceEntity;

public record ServiceResponse(
        int duration,
        String id,
        String serviceName,
        String categoryName,
        String description,
        double price,
        String status,
        String categoryId,
        String subCategoryId,
        String startTime,
        String endTime,
        String iconUrl,
        String subCategoryName,
        LocalDateTime createdAt) {
    public static ServiceResponse fromEntity(ServiceEntity s) {
        return new ServiceResponse(
                s.getDuration(),
                s.getId(),
                s.getName(),
                s.getCategory().getCategoryName(), s.getDescription(),
                s.getPrice(),
                s.getStatus(),
                s.getCategory().getId(),
                s.getSubCategory().getId(),
                s.getAvailableStartTimings(),
                s.getAvailableEndTiming(), s.getIconUrl(), s.getSubCategory().getSubCategoryName(), s.getCreatedAt());
    }
}
