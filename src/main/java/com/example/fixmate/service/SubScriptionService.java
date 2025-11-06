package com.example.fixmate.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.fixmate.entities.Subscription;
import com.example.fixmate.repositories.SubscriptionRepository;

@Service
public class SubScriptionService {
    @Autowired
    SubscriptionRepository subscriptionRepository;

    public List<Subscription> getPlan(String userId) {
        List<Subscription> plan = subscriptionRepository.findSubscriptionsByUserId(userId);
        return plan;
    }

}
