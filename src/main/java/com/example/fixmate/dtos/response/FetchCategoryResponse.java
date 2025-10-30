package com.example.fixmate.dtos.response;

import com.example.fixmate.entities.Category;

public record FetchCategoryResponse(
        String categoryId,
        String categoryName,
        String description,
        String status,
        String iconUrl,
        int subCategoriesCount) {
    public static FetchCategoryResponse fromEntity(Category c) {
        return new FetchCategoryResponse(c.getId(), c.getCategoryName(), c.getDescription(), c.getStatus(),
                c.getIconUrl(), c.getSubCategories().size());
    }
}
