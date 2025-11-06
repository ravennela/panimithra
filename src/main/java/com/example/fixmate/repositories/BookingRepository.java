package com.example.fixmate.repositories;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fixmate.entities.Bookings;

public interface BookingRepository extends JpaRepository<Bookings, String> {

    boolean existsByCustomer_IdAndService_IdAndBookingDateAndBookingStatusNot(
            String customerId,
            String serviceId,
            LocalDate bookingDate,
            String bookingStatus);

    Page<Bookings> findByEmployee_Id(String employeeId, Pageable pageable);

    Page<Bookings> findByCustomer_Id(String userId, Pageable pageable);

    Page<Bookings> findAll(Pageable pageable);

}
