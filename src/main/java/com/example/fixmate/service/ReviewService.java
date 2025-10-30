package com.example.fixmate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.fixmate.dtos.request.CreateReviewRequest;
import com.example.fixmate.dtos.response.CreateReviewResponse;
import com.example.fixmate.entities.Review;
import com.example.fixmate.entities.ServiceEntity;
import com.example.fixmate.entities.User;
import com.example.fixmate.repositories.ReviewRepository;
import com.example.fixmate.repositories.ServiceRepository;
import com.example.fixmate.repositories.UserRepository;

@Service
public class ReviewService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ServiceRepository repository;
    @Autowired
    ReviewRepository reviewRepository;

    public CreateReviewResponse createReview(CreateReviewRequest request) {

        User customer = userRepository.findById(request.getCustomerId()).orElse(null);
        if (customer == null) {
            throw new RuntimeException("Customer Not Found");
        }
        User employee = userRepository.findById(request.getEmployeeid()).orElse(null);
        if (employee == null) {
            throw new RuntimeException("Customer Not Found");
        }
        ServiceEntity sEntity = repository.findById(request.getServiceId()).orElse(null);
        if (sEntity == null) {
            throw new RuntimeException("Service Not Found");
        }

        Review review = new Review();
        review.setComment(request.getComment());
        review.setRating(request.getRating());
        review.setCustomer(customer);
        review.setEmployee(employee);
        review.setService(sEntity);
        reviewRepository.save(review);
        CreateReviewResponse response = new CreateReviewResponse();
        response.setId(review.getId());
        response.setMessage("Review Added Successfully");
        return response;

    }
}
