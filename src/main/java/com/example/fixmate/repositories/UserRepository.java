package com.example.fixmate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fixmate.entities.User;

public interface UserRepository extends JpaRepository<User, String> {
    User findByEmailId(String emailId);

    User findByContactNumber(String contactNumber);

}
