package com.example.fixmate.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.fixmate.entities.ServiceEntity;
import com.example.fixmate.entities.User;

public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    User findByEmailId(String emailId);

    User findByContactNumber(String contactNumber);

    Page<User> findAll(Pageable pageable);

}
