package com.example.fixmate.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import com.example.fixmate.dtos.custom.ServiceSpecification;
import com.example.fixmate.dtos.request.CreateServiceRequest;
import com.example.fixmate.dtos.response.CreateServiceResponse;
import com.example.fixmate.entities.Category;
import com.example.fixmate.entities.ServiceAvailableDate;
import com.example.fixmate.entities.ServiceEntity;
import com.example.fixmate.entities.ServiceImage;
import com.example.fixmate.entities.SubCategory;
import com.example.fixmate.entities.User;
import com.example.fixmate.repositories.CategoryRepository;
import com.example.fixmate.repositories.ServiceRepository;
import com.example.fixmate.repositories.SubCategoryRepository;
import com.example.fixmate.repositories.UserRepository;

@Service
public class ServicesService {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    SubCategoryRepository subCategoryRepository;

    @Autowired
    ServiceRepository serviceRepository;
    @Autowired
    UserRepository userRepository;

    public CreateServiceResponse createService(CreateServiceRequest request, String categoryId, String subCategoryId) {
        User employee = userRepository.findById(request.getEmployeeId()).orElse(null);
        if (employee == null) {
            throw new RuntimeException("User Not Found for this Service");
        }
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

        ServiceEntity service = new ServiceEntity();
        service.setAddress(request.getAddress());
        service.setDescription(request.getDescription());
        service.setEmployee(employee);
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

    public Page<ServiceEntity> getAllService(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return serviceRepository.findAll(pageable);
    }

    public Page<ServiceEntity> searchServices(
            String categoryId,
            String serviceName,
            Double minPrice,
            Double maxPrice,
            Double minRating,
            Pageable pageable, String categoryName, String subCategoryName) {
        Specification<ServiceEntity> spec = ServiceSpecification.filter(categoryId, serviceName, minPrice, maxPrice,
                minRating, categoryName, subCategoryName);

        return serviceRepository.findAll(spec, pageable);
    }
}
