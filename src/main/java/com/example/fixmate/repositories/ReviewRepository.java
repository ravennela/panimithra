package com.example.fixmate.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.fixmate.entities.Review;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, String> {
    boolean existsByCustomer_IdAndServiceId(String userId, String serviceId);

    List<Review> findTop5ByService_IdOrderByCreatedAtDesc(String serviceId);

    Page<Review> findByService_Id(String serviceId, Pageable pageable);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.service.id = :serviceId")
    long totalReviewsCount(@Param("serviceId") String serviceId);
}
