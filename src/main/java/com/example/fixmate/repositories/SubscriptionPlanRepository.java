package com.example.fixmate.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.fixmate.entities.SubscriptionPlan;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, String> {
    SubscriptionPlan findByPlanNameIgnoreCase(String planName);

    @Query("SELECT s FROM SubscriptionPlan s WHERE s.status= :status")
    List<SubscriptionPlan> getOnlyActivePlans(@Param("status") String status);
}
