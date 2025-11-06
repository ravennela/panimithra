package com.example.fixmate.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.fixmate.entities.Orders;

public interface OrdersRepository extends JpaRepository<Orders, String> {
    @Query("""
                SELECT o FROM Orders o
                WHERE o.subscription.id = :subscriptionId AND o.status = :status
                ORDER BY o.createdAt DESC
                LIMIT 1
            """)
    Optional<Orders> findTopBySubscriptionIdAndStatusOrderByCreatedAtDesc(
            @Param("subscriptionId") String subscriptionId,
            @Param("status") String status);

    Optional<Orders> findByRazorpayOrderId(String razorpayOrderId);

}
