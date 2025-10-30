package com.example.fixmate.dtos.response;

import com.example.fixmate.entities.SubCategory;

public record FetchSubCategoryResponse(
        String categoryId,
        String categoryName,
        String description,
        String status,
        String iconUrl) {
    public static FetchSubCategoryResponse fromEntity(SubCategory c) {
        return new FetchSubCategoryResponse(c.getId(), c.getSubCategoryName(), c.getDescription(), c.getStatus(),
                c.getIconUrl());
    }
}
