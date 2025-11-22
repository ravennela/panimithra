package com.example.fixmate.dtos.response;

import com.example.fixmate.entities.SubCategory;

public record FetchSubCategoryByIdResponse(
        String categoryId,
        String subCategoryId,
        String categoryName,
        String subCategoryname,
        String status,
        String iconUrl,
        String description
    ) {
    static public FetchSubCategoryByIdResponse fromEntity(SubCategory s) {
        return new FetchSubCategoryByIdResponse(s.getCategory().getId(), s.getId(), s.getCategory().getCategoryName(),
                s.getSubCategoryName(), s.getStatus(), s.getIconUrl(),s.getDescription());
    }
}
