package com.example.fixmate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fixmate.entities.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, String> {
    boolean existsByCustomer_IdAndServiceId(String userId, String serviceId);

    List<Review> findTop5ByService_IdOrderByCreatedAtDesc(String serviceId);
}
