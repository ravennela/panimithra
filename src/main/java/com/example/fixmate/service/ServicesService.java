package com.example.fixmate.service;

import java.util.ArrayList;
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
import com.example.fixmate.dtos.response.ServiceByIdResponse;
import com.example.fixmate.entities.Category;
import com.example.fixmate.entities.Review;
import com.example.fixmate.entities.ServiceAvailableDate;
import com.example.fixmate.entities.ServiceEntity;
import com.example.fixmate.entities.ServiceImage;
import com.example.fixmate.entities.SubCategory;
import com.example.fixmate.entities.User;
import com.example.fixmate.repositories.CategoryRepository;
import com.example.fixmate.repositories.ReviewRepository;
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
    @Autowired
    ReviewRepository reviewRepository;

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
        service.setAddInfoOne(request.getAddInfoOne());
        service.setAddInfoTwo(request.getAddInfoTwo());
        service.setAddInfoThree(request.getAddInfoThree());
        service.setAvailableStartTimings(request.getAvailableStartTime());
        service.setAvailableEndTiming(request.getAvailableEndTime());
        service.setTimeIn(request.getTimeIn());
        service.setTimeOut(request.getTimeOut());
        service.setIconUrl(request.getIconUrl());
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

    public CreateServiceResponse updateService(String serviceId, CreateServiceRequest request, String categoryId,
            String subCategoryId) {

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
        ServiceEntity service = serviceRepository.findById(serviceId).orElse(null);
        if (service == null) {
            throw new RuntimeException("No Service Found");
        }
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
        service.setAddInfoOne(request.getAddInfoOne());
        service.setAddInfoTwo(request.getAddInfoTwo());
        service.setAddInfoThree(request.getAddInfoThree());
        service.setAvailableStartTimings(request.getAvailableStartTime());
        service.setAvailableEndTiming(request.getAvailableEndTime());
        service.setTimeIn(request.getTimeIn());
        service.setTimeOut(request.getTimeOut());
        service.setIconUrl(request.getIconUrl());
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

        if (service.getAvailableDates() != null) {
            service.getAvailableDates().clear();
            service.getAvailableDates().addAll(availableDates);
        } else {
            service.setAvailableDates(availableDates);
        }

        // Update images safely
        if (service.getImages() != null) {
            service.getImages().clear();
            service.getImages().addAll(images);
        } else {
            service.setImages(images);
        }
        serviceRepository.save(service);

        CreateServiceResponse response = new CreateServiceResponse();
        response.setMessage("Service Created Successfully");
        response.setId(service.getId());
        return response;
    }

    public Page<ServiceEntity> getAllService(int page, int size, String sortBy, String direction, String employeeId) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return serviceRepository.findByEmployee_Id(employeeId, pageable);
    }

    public Page<ServiceEntity> searchServices(
            String categoryId,
            String serviceName,
            Double minPrice,
            Double maxPrice,
            Double minRating,
            Pageable pageable, String categoryName, String subCategoryName, Double latitude, Double longitude,
            Double radious) {
        Specification<ServiceEntity> spec = ServiceSpecification.filter(categoryId, serviceName, minPrice, maxPrice,
                minRating, categoryName, subCategoryName, latitude, longitude, radious);

        return serviceRepository.findAll(spec, pageable);
    }

    public ServiceByIdResponse fetchService(String serviceId) {
        ServiceEntity service = serviceRepository.findById(serviceId).orElse(null);
        if (service == null) {
            throw new RuntimeException("No Service Found");
        }

        List<ServiceAvailableDate> dates = serviceRepository.getAvailableDateByService(serviceId);
        List<String> oList = new ArrayList<>();

        for (int i = 0; i < dates.size(); i++) {
            oList.add(dates.get(i).getAvailableDate()); // ✅ use .get(index) and getter method
        }

        long totalCount = reviewRepository.totalReviewsCount(serviceId);
        ServiceByIdResponse response = new ServiceByIdResponse();
        response.setDescription(service.getDescription());
        response.setPrice(service.getPrice());
        response.setEmployeeExperiance(service.getEmployee().getExperiance());
        response.setEmployeeName(service.getEmployee().getName());
        response.setServiceId(service.getId());
        response.setServiceName(service.getName());
        response.setReviews(service.getReviews());
        response.setEmployeeId(service.getEmployee().getId());
        response.setAddInfoOne(service.getAddInfoOne());
        response.setAddInfoTwo(service.getAddInfoTwo());
        response.setCategoryName(service.getCategory().getCategoryName());
        response.setSubCategoryName(service.getSubCategory().getSubCategoryName());
        response.setAddInfoThree(service.getAddInfoThree());
        response.setAvaragerating(calculateAverageRating(service.getReviews()));
        response.setIconUrl(service.getIconUrl());
        response.setAddress(service.getAddress());
        response.setCategoryId(service.getCategory().getId());
        response.setSubCategoryId(service.getSubCategory().getId());
        response.setStartTime(service.getAvailableStartTimings());
        response.setEndTime(service.getAvailableEndTiming());
        response.setTimeIn(service.getTimeIn());
        response.setTimeOut(service.getTimeOut());
        response.setTotalReviewCount(totalCount);
        response.setAvailableDates(oList);
        return response;
    }

    public static double calculateAverageRating(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty())
            return 0.0;
        return reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);
    }

}
