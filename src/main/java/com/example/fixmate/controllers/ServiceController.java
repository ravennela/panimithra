package com.example.fixmate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.fixmate.dtos.custom.ApiErrorDto;
import com.example.fixmate.dtos.request.CreateServiceRequest;
import com.example.fixmate.dtos.response.CreateServiceResponse;
import com.example.fixmate.service.ServicesService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/service")
public class ServiceController {
    @Autowired
    ServicesService service;

    @PostMapping("/create-service")
    @PreAuthorize("hasAuthority('ADMIN')")
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

}
