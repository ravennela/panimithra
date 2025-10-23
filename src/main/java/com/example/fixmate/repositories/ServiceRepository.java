package com.example.fixmate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fixmate.entities.Service;

public interface ServiceRepository extends JpaRepository<Service, String> {

}
