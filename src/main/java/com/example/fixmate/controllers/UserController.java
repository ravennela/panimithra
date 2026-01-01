package com.example.fixmate.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.fixmate.dtos.custom.ApiErrorDto;
import com.example.fixmate.dtos.request.AuthRequest;
import com.example.fixmate.dtos.request.CreateUserRequest;
import com.example.fixmate.dtos.request.ResetBeforeAuthRequest;
import com.example.fixmate.dtos.request.ResetPasswordRequest;
import com.example.fixmate.dtos.response.CreateSubcategoryResponse;
import com.example.fixmate.dtos.response.CreateUserResponse;
import com.example.fixmate.dtos.response.FetchUserResponse;
import com.example.fixmate.dtos.response.GetUserResponse;
import com.example.fixmate.dtos.response.UserProfileResponse;
import com.example.fixmate.entities.User;
import com.example.fixmate.service.UserService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    UserService service;

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        try {
            CreateUserResponse response = service.createUser(request);
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            GetUserResponse response = service.login(request);
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

    @GetMapping("/getAllUsers")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> fetchAllUsers(@RequestParam(required = false) String name,
            @RequestParam(required = false) String status, String[] sort,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "0", required = false) int page, @RequestParam(required = false) String role) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(getSortOrders(sort)));
            Page<User> userPage = service.fetchUsers(page, name, status, role, pageable);
            List<FetchUserResponse> data = userPage.getContent().stream().map(FetchUserResponse::fromEntity).toList();
            Map<String, Object> response = new HashMap<>();
            response.put("data", data);
            response.put("currentPage", userPage.getNumber());
            response.put("totalItems", userPage.getTotalElements());
            response.put("totalPages", userPage.getTotalPages());
            response.put("pageSize", userPage.getSize());
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

    public List<Sort.Order> getSortOrders(String[] sort) {
        List<Sort.Order> orders = new ArrayList<>();

        // Fallback default: createdAt DESC
        if (sort == null || sort.length == 0) {
            orders.add(new Sort.Order(Sort.Direction.DESC, "createdAt"));
            return orders;
        }

        for (String sortParam : sort) {
            if (sortParam == null || sortParam.isBlank()) {
                continue;
            }
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

    @GetMapping("/user-profile")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('EMPLOYEE')")
    public ResponseEntity<?> userProfile(@RequestParam String userId) {
        try {
            User user = service.userprofile(userId);
            UserProfileResponse response = UserProfileResponse.fromEntity(user);
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

    @DeleteMapping("/delete-user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteUser(@RequestParam String userId) {
        try {
            CreateSubcategoryResponse response = service.deleteUser(userId);
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

    @PutMapping("/change-user-status")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> changeUserStatus(@RequestParam String userId, @RequestParam String status) {
        try {
            CreateSubcategoryResponse response = service.changeUserStatus(userId, status);
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

    @GetMapping("/faq")
    public ResponseEntity<?> faqController() {
        try {
            List<HashMap<String, String>> data = service.faqService();
            return ResponseEntity.ok(data);
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

    @PostMapping("/reset-password")
    public ResponseEntity<?> changePassword(@RequestBody ResetPasswordRequest body) {
        try {
            CreateSubcategoryResponse response = service.changePassword(body);
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

    @PostMapping("/reset-before-auth")
    public ResponseEntity<?> resetBeforeAuth(@RequestBody ResetBeforeAuthRequest body) {
        try {
            CreateSubcategoryResponse response = service.resetBeforeAuth(body);
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
