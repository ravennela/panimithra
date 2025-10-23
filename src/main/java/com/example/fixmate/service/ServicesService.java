package com.example.fixmate.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.fixmate.dtos.request.CreateServiceRequest;
import com.example.fixmate.dtos.response.CreateServiceResponse;
import com.example.fixmate.entities.Category;
import com.example.fixmate.entities.ServiceAvailableDate;
import com.example.fixmate.entities.ServiceImage;
import com.example.fixmate.entities.SubCategory;
import com.example.fixmate.repositories.CategoryRepository;
import com.example.fixmate.repositories.ServiceRepository;
import com.example.fixmate.repositories.SubCategoryRepository;

@Service
public class ServicesService {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    SubCategoryRepository subCategoryRepository;

    @Autowired
    ServiceRepository serviceRepository;

    public CreateServiceResponse createService(CreateServiceRequest request, String categoryId, String subCategoryId) {

        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            throw new RuntimeException("No Category Found");
        }
        SubCategory subCategory = subCategoryRepository.findById(subCategoryId).orElse(null);
        if (subCategoryId != null) {
            if (subCategory == null) {
                throw new RuntimeException("No Sub Category Found");
            }
        }

        com.example.fixmate.entities.Service service = new com.example.fixmate.entities.Service();
        service.setAddress(request.getAddress());
        service.setDescription(request.getDescription());

        service.setLatitude(request.getLatitude());
        service.setLongitude(request.getLongitude());
        service.setDuration(request.getDuration());
        service.setName(request.getName());
        service.setPrice(request.getPrice());
        service.setCategory(category);
        service.setSubCategory(subCategory);
        service.setStatus(request.getStatus());

        List<ServiceAvailableDate> availableDates = request.getAvailableDates().stream()
                .map(dto -> {
                    ServiceAvailableDate date = new ServiceAvailableDate();
                    date.setAvailableDate(dto.getAvailableDate());
                    date.setService(service);
                    return date;
                })
                .collect(Collectors.toList());

        // Map images
        List<ServiceImage> images = request.getImages().stream()
                .map(dto -> {
                    ServiceImage img = new ServiceImage();
                    img.setImageUrl(dto.getImageUrls());
                    img.setService(service);
                    return img;
                })
                .collect(Collectors.toList());
        service.setAvailableDates(availableDates);
        service.setImages(images);
        serviceRepository.save(service);

        CreateServiceResponse response = new CreateServiceResponse();
        response.setMessage("Service Created Successfully");
        response.setId(service.getId());
        return response;
    }

}
