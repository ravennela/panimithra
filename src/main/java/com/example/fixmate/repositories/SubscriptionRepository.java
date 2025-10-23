package com.example.fixmate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fixmate.entities.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, String> {

}
