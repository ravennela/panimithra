package com.example.fixmate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.fixmate.entities.ServiceAvailableDate;
import com.example.fixmate.entities.ServiceEntity;

import org.springframework.data.domain.Pageable;

import java.util.List;

import org.springframework.data.domain.Page;

public interface ServiceRepository
        extends JpaRepository<ServiceEntity, String>, JpaSpecificationExecutor<ServiceEntity> {

    Page<ServiceEntity> findAll(Pageable pageable);

    @Query("SELECT s.availableDates FROM ServiceEntity s WHERE s.id = :serviceId")
    List<ServiceAvailableDate> getAvailableDateByService(@Param("serviceId") String serviceId);
}
