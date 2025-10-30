package com.example.fixmate.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.fixmate.entities.Category;
import com.example.fixmate.entities.ServiceEntity;
import com.example.fixmate.dtos.custom.ApiErrorDto;
import com.example.fixmate.dtos.request.CreateServiceRequest;
import com.example.fixmate.dtos.response.CreateServiceResponse;
import com.example.fixmate.dtos.response.FetchCategoryResponse;
import com.example.fixmate.dtos.response.SearchServiceResponse;
import com.example.fixmate.dtos.response.ServiceResponse;
import com.example.fixmate.service.ServicesService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/service")
public class ServiceController {
    @Autowired
    ServicesService service;

    @PostMapping("/create-service")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('EMPLOYEE')")
    public ResponseEntity<?> createService(@RequestBody CreateServiceRequest request, @RequestParam String catId,
            @RequestParam String subCatId) {
        try {

            CreateServiceResponse response = service.createService(request, catId, subCatId);
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

    @GetMapping("/getAllService")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<?> getAllServices(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        try {
            Page<ServiceEntity> servicePage = service.getAllService(page, size, sortBy, direction);
            List<ServiceResponse> data = servicePage.getContent()
                    .stream()
                    .map(ServiceResponse::fromEntity)
                    .toList();
            Map<String, Object> response = new HashMap<>();
            response.put("data", data);
            response.put("currentPage", servicePage.getNumber());
            response.put("totalItems", servicePage.getTotalElements());
            response.put("totalPages", servicePage.getTotalPages());
            response.put("pageSize", servicePage.getSize());
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

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('EMPLOYEE')")
    public ResponseEntity<?> searchServices(
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String serviceName,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Double minRating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String[] sort,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String subCategoryName) {
        // Parse sort parameters
        try {

            Pageable pageable = PageRequest.of(page, size, Sort.by(getSortOrders(sort)));

            Page<ServiceEntity> results = service.searchServices(
                    categoryId, serviceName, minPrice, maxPrice, minRating, pageable, categoryName, subCategoryName);

            List<SearchServiceResponse> data = results.getContent().stream().map(SearchServiceResponse::fromEntity)
                    .toList();
            Map<String, Object> output = new HashMap<>();
            output.put("data", data);
            output.put("currentPage", results.getNumber());
            output.put("totalItems", results.getTotalElements());
            output.put("totalPages", results.getTotalPages());
            output.put("pageSize", results.getSize());

            return ResponseEntity.ok(output);
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

    private List<Sort.Order> getSortOrders(String[] sort) {
        List<Sort.Order> orders = new ArrayList<>();

        // Fallback default: createdAt DESC
        if (sort == null || sort.length == 0) {
            orders.add(new Sort.Order(Sort.Direction.DESC, "createdAt"));
            return orders;
        }

        for (String sortParam : sort) {
            if (sortParam == null || sortParam.isBlank())
                continue;

            String[] parts = sortParam.split(",");
            String field = parts[0].trim();
            String direction = (parts.length > 1 ? parts[1].trim() : "asc");

            try {
                orders.add(new Sort.Order(Sort.Direction.fromString(direction), field));
            } catch (IllegalArgumentException e) {
                // Invalid direction (e.g., "ascending"), ignore and fallback to ASC
                orders.add(new Sort.Order(Sort.Direction.ASC, field));
            }
        }

        // Still empty? fallback again
        if (orders.isEmpty()) {
            orders.add(new Sort.Order(Sort.Direction.DESC, "createdAt"));
        }

        return orders;
    }
}