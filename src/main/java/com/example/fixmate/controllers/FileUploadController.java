package com.example.fixmate.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.fixmate.service.GCSUploadService;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {

    private final GCSUploadService uploadService;

    public FileUploadController(GCSUploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("api called");
            String url = uploadService.uploadFile(file);
            System.out.println("url" + url);
            return ResponseEntity.ok(Map.of("imageUrl", url));
        } catch (Exception e) {
            System.out.println(e.toString());
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}
