package com.example.fixmate.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

@Service
public class GCSUploadService {

    @Value("${gcp.bucket.name}")
    private String bucketName;

    private final Storage storage;

    public GCSUploadService(Storage storage) {
        this.storage = storage;
    }

    public String uploadFile(MultipartFile file) throws IOException {
       

        // Generate unique file name
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        BlobId blobId = BlobId.of(bucketName, fileName);
        System.out.println("blob id" + blobId);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();
        storage.create(blobInfo, file.getBytes());
        String publicUrl = String.format(
                "https://storage.googleapis.com/%s/%s",
                bucketName,
                fileName);
        return publicUrl;
    }

}
