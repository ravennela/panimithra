package com.example.fixmate.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.fixmate.dtos.response.CityBookingCountDTO;
import com.example.fixmate.dtos.response.MonthWiseRevenue;
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

    @Query("SELECT COUNT(b) from Bookings b WHERE MONTH (b.createdAt) =:month AND YEAR (b.createdAt)=:year AND b.bookingStatus = :status")
    long pendingBoookingsInMonth(@Param("month") int month, @Param("year") int year, @Param("status") String status);

    @Query("SELECT SUM(s.totalAmount) FROM Bookings s " +
            "WHERE MONTH(s.createdAt) = :month " +
            "AND YEAR(s.createdAt) = :year " +
            "AND s.bookingStatus = :status")
    Double revenueByTheCurrentMonth(@Param("month") int month,
            @Param("year") int year,
            @Param("status") String status);

    @Query("SELECT new com.example.fixmate.dtos.response.CityBookingCountDTO(c.city, COUNT(b)) " +
            "FROM Bookings b JOIN b.customer c " +
            "WHERE MONTH(b.createdAt) = :month AND YEAR(b.createdAt) = :year AND b.bookingStatus = :status " +
            "GROUP BY c.city")
    List<CityBookingCountDTO> countBookingsByUserCityAndMonth(
            @Param("month") int month,
            @Param("year") int year,
            @Param("status") String status);

    @Query("SELECT Count(b) FROM Bookings b")
    long findBookingCount();

    @Query("SELECT COUNT(b) FROM Bookings b WHERE b.bookingStatus = :status")
    long countBookingsByStatus(@Param("status") String status);

    @Query("SELECT new com.example.fixmate.dtos.response.CityBookingCountDTO(u.city, COUNT(b)) " +
            "FROM Bookings b " +
            "JOIN b.customer u " + // 👈 change 'user' to whatever is correct
            "GROUP BY u.city")
    List<CityBookingCountDTO> overAllBookingsByCity();

    @Query("SELECT Count(b) From Bookings b JOIN b.employee e where e.id=:userId")
    long countOfBookingByUserId(@Param("userId") String userId);

    @Query("SELECT Count(b) From Bookings b JOIN b.employee e where e.id=:userId AND b.bookingStatus=:status")
    long employeeInprogessBookingCount(@Param("userId") String userId, @Param("status") String status);

    @Query("SELECT SUM(b.totalAmount) FROM Bookings b " +
            "JOIN b.employee e " +
            "WHERE e.id = :userId " +
            "AND MONTH(b.createdAt) = :month " +
            "AND YEAR(b.createdAt) = :year " +
            "AND b.bookingStatus = 'COMPLETED'")
    Double employeeCurrentMonthRevenue(
            @Param("userId") String userId,
            @Param("month") int month,
            @Param("year") int year);

    @Query("SELECT new com.example.fixmate.dtos.response.MonthWiseRevenue(" +
            "MONTH(b.createdAt), SUM(b.totalAmount)) " +
            "FROM Bookings b " +
            "JOIN b.employee e " +
            "WHERE e.id = :userId " +
            "AND b.bookingStatus = 'COMPLETED' " +
            "AND YEAR(b.createdAt) = :year " +
            "GROUP BY MONTH(b.createdAt) " +
            "ORDER BY MONTH(b.createdAt)")
    List<MonthWiseRevenue> getEmployeeMonthlyRevenue(
            @Param("userId") String userId,
            @Param("year") int year);

}
