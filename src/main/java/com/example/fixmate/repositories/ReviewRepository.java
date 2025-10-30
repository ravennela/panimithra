package com.example.fixmate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fixmate.entities.Review;

public interface ReviewRepository extends JpaRepository<Review, String> {

}
