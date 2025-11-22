package com.example.fixmate.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.fixmate.dtos.request.CreateSubCategoryRequest;
import com.example.fixmate.dtos.request.UpdateSubCategoryRequest;
import com.example.fixmate.dtos.response.CreateSubcategoryResponse;
import com.example.fixmate.dtos.response.FetchSubCategoryByIdResponse;
import com.example.fixmate.entities.Category;
import com.example.fixmate.entities.SubCategory;
import com.example.fixmate.repositories.CategoryRepository;
import com.example.fixmate.repositories.SubCategoryRepository;

@Service
public class SubCategoryService {
    @Autowired
    CategoryRepository repository;
    @Autowired
    SubCategoryRepository subCategoryRepository;

    public CreateSubcategoryResponse createSubcategory(CreateSubCategoryRequest request, String categoryId) {

        Category category = repository.findById(categoryId).orElse(null);
        if (category == null) {
            throw new RuntimeException("Category Not Found");
        }
        SubCategory subCategory = new SubCategory();
        subCategory.setSubCategoryName(request.getName());
        subCategory.setDescription(request.getDescription());
        subCategory.setIconUrl(request.getIconUrl());
        subCategory.setStatus(request.getStatus());
        subCategory.setCategory(category);
        subCategoryRepository.save(subCategory);

        CreateSubcategoryResponse response = new CreateSubcategoryResponse();
        response.setMessage("Sub category created successfully");
        response.setId(subCategory.getId());
        return response;
    }

    public Page<SubCategory> fetchSubCategory(int page, int size, String sortBy, String direction, String categoryId) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return subCategoryRepository.findByCategoryId(categoryId, pageable);
    }

    public CreateSubcategoryResponse deleteSubCategory(String subCategoryId) {
        SubCategory subCategory = subCategoryRepository.findById(subCategoryId).orElse(null);
        if (subCategory == null) {
            throw new RuntimeException("No Subcategory Found");
        }
        subCategoryRepository.delete(subCategory);
        CreateSubcategoryResponse response = new CreateSubcategoryResponse();
        response.setId(subCategoryId);
        response.setMessage("Subcategory Deleted Successfully");
        return response;
    }

    public SubCategory fetchSubCategoryById(String subCategoryId) {
        SubCategory subCategory = subCategoryRepository.findById(subCategoryId).orElse(null);
        if (subCategory == null) {
            throw new RuntimeException("No Subcategory Found");
        }
        return subCategory;
    }

    public CreateSubcategoryResponse updateSubCategory(UpdateSubCategoryRequest request) {
        SubCategory subCategory = subCategoryRepository.findById(request.getSubCategoryId()).orElse(null);
        if (subCategory == null) {
            throw new RuntimeException("No Subcategories Found");
        }
        subCategory.setSubCategoryName(request.getSubCategoryName());
        subCategory.setDescription(request.getDescription());
        subCategory.setStatus(request.getStatus());
        if (request.getIconUrl() != null && !request.getIconUrl().isBlank()) {
            subCategory.setIconUrl(request.getIconUrl());
        }
        subCategoryRepository.save(subCategory);
        CreateSubcategoryResponse response = new CreateSubcategoryResponse();
        response.setId(subCategory.getId());
        response.setMessage("Category Updated Successfully");
        return response;
    }
}
