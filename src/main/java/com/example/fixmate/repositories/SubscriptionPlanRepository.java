package com.example.fixmate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fixmate.entities.SubscriptionPlan;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, String> {
    SubscriptionPlan findByPlanNameIgnoreCase(String planName);
}
