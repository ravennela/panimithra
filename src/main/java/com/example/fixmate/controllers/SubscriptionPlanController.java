package com.example.fixmate.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fixmate.dtos.custom.ApiErrorDto;
import com.example.fixmate.dtos.request.CreateSubscriptionPlanRequest;
import com.example.fixmate.dtos.request.CreateSubscriptionPlanResponse;
import com.example.fixmate.service.SubscriptionPlanService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
public class SubscriptionPlanController {
    @Autowired
    SubscriptionPlanService service;

    @PostMapping("/plan-creation")
    public ResponseEntity<?> createPlan(@RequestBody CreateSubscriptionPlanRequest entity) {
        try {
            CreateSubscriptionPlanResponse response = service.createSubscriptionPlan(entity);
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
