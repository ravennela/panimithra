package com.example.fixmate.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.fixmate.dtos.custom.ApiErrorDto;
import com.example.fixmate.dtos.request.CategoryCreationRequest;
import com.example.fixmate.dtos.response.CategoryCreationResponse;
import com.example.fixmate.dtos.response.FetchCategoryResponse;
import com.example.fixmate.entities.Category;
import com.example.fixmate.service.CategoryService;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    CategoryService service;

    @PostMapping("/create-category")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createCategory(@RequestBody CategoryCreationRequest entity) {
        try {
            CategoryCreationResponse response = service.create(entity);
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

    @GetMapping("/fetch-category")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('EMPLOYEE')")
    public ResponseEntity<?> getAllCategories(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        try {
            Page<Category> catPage = service.fetchCategory(page, size, sortBy, direction);
            List<FetchCategoryResponse> data = catPage.getContent().stream().map(FetchCategoryResponse::fromEntity)
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
