package com.example.fixmate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.fixmate.entities.ServiceEntity;

import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Page;

public interface ServiceRepository
        extends JpaRepository<ServiceEntity, String>, JpaSpecificationExecutor<ServiceEntity> {

    Page<ServiceEntity> findAll(Pageable pageable);

}
