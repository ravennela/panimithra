package com.example.fixmate.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.fixmate.dtos.custom.ApiErrorDto;
import com.example.fixmate.dtos.request.CreateSubCategoryRequest;
import com.example.fixmate.dtos.response.CreateSubcategoryResponse;
import com.example.fixmate.dtos.response.FetchCategoryResponse;
import com.example.fixmate.dtos.response.FetchSubCategoryResponse;
import com.example.fixmate.entities.Category;
import com.example.fixmate.entities.SubCategory;
import com.example.fixmate.service.SubCategoryService;

import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/subcategory")
public class SubcategoryController {
    @Autowired
    SubCategoryService service;

    @PostMapping("/create-subcategory")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createSubCategory(@RequestBody CreateSubCategoryRequest request,
            @RequestParam String categoryId) {
        try {
            CreateSubcategoryResponse response = service.createSubcategory(request, categoryId);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException exception) {
            ApiErrorDto response = new ApiErrorDto();
            System.out.println(exception.getStackTrace());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setError(exception.getMessage());
            System.out.println(exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception exception) {
            ApiErrorDto response = new ApiErrorDto();
            System.out.println(exception.getStackTrace());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setError(exception.getMessage());
            System.out.println(exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

    }

    @GetMapping("/fetch-sub-category")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('EMPLOYEE')")
    public ResponseEntity<?> getAllCategories(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction, @RequestParam String categoryId) {

        try {
            Page<SubCategory> catPage = service.fetchSubCategory(page, size, sortBy, direction, categoryId);
            List<FetchSubCategoryResponse> data = catPage.getContent().stream()
                    .map(FetchSubCategoryResponse::fromEntity)
                    .toList();

            Map<String, Object> response = new HashMap<>();
            response.put("data", data);
            response.put("currentPage", catPage.getNumber());
            response.put("totalItems", catPage.getTotalElements());
            response.put("totalPages", catPage.getTotalPages());
            response.put("pageSize", catPage.getSize());
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException exception) {
            ApiErrorDto response = new ApiErrorDto();
            System.out.println(exception.getStackTrace());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setError(exception.getMessage());
            System.out.println(exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception exception) {
            ApiErrorDto response = new ApiErrorDto();
            System.out.println(exception.getStackTrace());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setError(exception.getMessage());
            System.out.println(exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

}
