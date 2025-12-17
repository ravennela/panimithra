package com.example.fixmate.dtos.response;

import com.example.fixmate.entities.Category;

public record FetchCategoryByIdResponse(
        String categoryId,
        String categoryName,
        String status,
        String iconUrl,
        String description) {   
    public static FetchCategoryByIdResponse fromEntity(Category category) {
        return new FetchCategoryByIdResponse(category.getId(), category.getCategoryName(), category.getStatus(),
                category.getIconUrl(), category.getDescription());
    }
}
