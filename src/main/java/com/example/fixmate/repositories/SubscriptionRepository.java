package com.example.fixmate.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.fixmate.entities.Subscription;
import com.example.fixmate.entities.User;

public interface SubscriptionRepository extends JpaRepository<Subscription, String> {

        @Query("""
                        SELECT s
                        FROM Subscription s
                        JOIN FETCH s.employee e
                        JOIN FETCH s.subscriptionPlan p
                        WHERE e.id = :userId
                        """)
        List<Subscription> findSubscriptionsByUserId(@Param("userId") String userId);

        @Query("""
                        SELECT s
                        FROM Subscription s
                        WHERE s.employee.id = :userId
                        AND :date BETWEEN s.startDate AND s.endDate
                        AND s.status = 'ACTIVE'
                        """)
        Optional<Subscription> findActiveByEmployee_Id(
                        @Param("userId") String userId,
                        @Param("date") LocalDate date);

        Subscription findTopByEmployee_IdOrderByStartDateDesc(String userId);

        @Query("SELECT e FROM User e   Join e.subscription  s where s.endDate < CURRENT_DATE and e.status ='ACTIVE'")
        List<User> findEmployeesWhosSubscriptionExpired();

}
