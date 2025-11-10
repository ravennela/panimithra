package com.example.fixmate.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.fixmate.dtos.response.CityEmployeeCountDTO;
import com.example.fixmate.entities.User;

public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    User findByEmailId(String emailId);

    User findByContactNumber(String contactNumber);

    Page<User> findAll(Pageable pageable);

    @Query("SELECT COUNT(u) FROM User u " +
            "WHERE MONTH(u.createdAt) = :month " +
            "AND YEAR(u.createdAt) = :year " +
            "AND u.role = :role")
    long countUsersRegisteredInMonthByRole(@Param("month") int month,
            @Param("year") int year,
            @Param("role") String role);

    @Query("SELECT COUNT(u) FROM User u WHERE MONTH(u.createdAt)=:month AND YEAR(u.createdAt) =:year AND u.role=:role")
    long countEmployeeRegistredInMonth(@Param("month") int month, @Param("year") int year, @Param("role") String role);

    @Query("SELECT new com.example.fixmate.dtos.response.CityEmployeeCountDTO(u.city, COUNT(u)) FROM User u WHERE  u.role=:role GROUP BY u.city")
    List<CityEmployeeCountDTO> findEmployeeCountGroupedByCity(@Param("role") String role);
    
}
