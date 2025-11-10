package com.example.fixmate.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.fixmate.dtos.request.CreateReviewRequest;
import com.example.fixmate.dtos.response.CreateReviewResponse;
import com.example.fixmate.dtos.response.TopFiveReviewsResponse;
import com.example.fixmate.entities.Bookings;
import com.example.fixmate.entities.Review;
import com.example.fixmate.entities.ServiceEntity;
import com.example.fixmate.entities.User;
import com.example.fixmate.repositories.BookingRepository;
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
    @Autowired
    BookingRepository bookingRepository;

    public CreateReviewResponse createReview(CreateReviewRequest request) {
        Bookings bookings = bookingRepository.findById(request.getBookingId()).orElse(null);
        if (bookings == null) {
            throw new RuntimeException("No Bookings Found with this Id");
        }
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

        if (reviewRepository.existsByCustomer_IdAndServiceId(request.getCustomerId(),
                request.getServiceId())) {
            throw new IllegalArgumentException("You have already reviewed this service.");
        }
        Review review = new Review();
        review.setComment(request.getComment());
        review.setRating(request.getRating());
        review.setCustomer(customer);
        review.setEmployee(employee);
        review.setBookings(bookings);
        review.setService(sEntity);
        reviewRepository.save(review);
        CreateReviewResponse response = new CreateReviewResponse();
        response.setId(review.getId());
        response.setMessage("Review Added Successfully");
        return response;

    }

    public List<Review> reviewsResponse(String serviceId) {
        List<Review> topFiveRatingds = reviewRepository.findTop5ByService_IdOrderByCreatedAtDesc(serviceId);

        return topFiveRatingds;
    }

    public Page<Review> findReviewByServiceId(String serviceId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        return reviewRepository.findByService_Id(serviceId, pageable);
    }
}
