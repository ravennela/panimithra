package com.example.fixmate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.fixmate.dtos.request.CategoryCreationRequest;
import com.example.fixmate.dtos.response.CategoryCreationResponse;
import com.example.fixmate.entities.Category;
import com.example.fixmate.repositories.CategoryRepository;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository repository;

    public CategoryCreationResponse create(CategoryCreationRequest request) {
        Category category = new Category();
        category.setCategoryName(request.getCategoryName());
        category.setDescription(request.getDescription());
        category.setIconUrl(request.getIconUrl());
        category.setStatus(request.getStatus());
        repository.save(category);

        CategoryCreationResponse response = new CategoryCreationResponse();
        response.setMessage("Category Created Successfully");
        response.setId(category.getId());
        return response;

    }

    public Page<Category> fetchCategory(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findAll(pageable);
    }

}
