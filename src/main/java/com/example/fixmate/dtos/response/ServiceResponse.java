package com.example.fixmate.dtos.response;

import java.util.List;

import com.example.fixmate.entities.ServiceEntity;
import com.example.fixmate.entities.ServiceImage;

public record ServiceResponse(
        String id,
        String serviceName,
        String categoryName,
        String description,
        double price,
        String status,
        String categoryId,
        String subCategoryId

) {
    public static ServiceResponse fromEntity(ServiceEntity s) {
        return new ServiceResponse(
                s.getId(),
                s.getName(),
                s.getCategory().getCategoryName(), s.getDescription(),
                s.getPrice(),
                s.getStatus(),
                s.getCategory().getId(),
                s.getSubCategory().getId()

        );
    }
}
