package com.example.fixmate.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fixmate.entities.EmailOtp;

public interface OtpRepository extends JpaRepository<EmailOtp, String> {

    Optional<EmailOtp> findByEmail(String email);
}
