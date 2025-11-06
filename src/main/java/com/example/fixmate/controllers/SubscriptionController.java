package com.example.fixmate.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.fixmate.dtos.custom.ApiErrorDto;
import com.example.fixmate.dtos.response.FetchUserSubscriptionResponse;
import com.example.fixmate.entities.Subscription;
import com.example.fixmate.service.SubScriptionService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/subcription")
public class SubscriptionController {
    @Autowired
    SubScriptionService service;

    @GetMapping("/employee-plan")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<?> fetchPlans(@RequestParam String userId) {
        try {
            List<Subscription> plans = service.getPlan(userId);
            List<FetchUserSubscriptionResponse> data = plans.stream().map(FetchUserSubscriptionResponse::fromEntity)
                    .toList();
            Map<String, Object> plan = new HashMap<>();
            plan.put("plan", data);
            return ResponseEntity.ok(plan);

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
