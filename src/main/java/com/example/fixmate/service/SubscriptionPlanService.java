package com.example.fixmate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.fixmate.dtos.request.CreateSubscriptionPlanRequest;
import com.example.fixmate.dtos.request.CreateSubscriptionPlanResponse;
import com.example.fixmate.entities.SubscriptionPlan;
import com.example.fixmate.repositories.SubscriptionPlanRepository;

@Service
public class SubscriptionPlanService {

    @Autowired
    SubscriptionPlanRepository repository;

    public CreateSubscriptionPlanResponse createSubscriptionPlan(CreateSubscriptionPlanRequest request) {

        SubscriptionPlan plan = new SubscriptionPlan();

        plan.setPlanName(request.getPlanName());
        plan.setDescription(request.getDescription());
        plan.setPrice(request.getPrice());
        plan.setDurationInDays(request.getDurationInDays());
        plan.setStatus(request.getStatus());
        repository.save(plan);
        CreateSubscriptionPlanResponse response = new CreateSubscriptionPlanResponse();
        response.setMessage("Plan Created Succefully");
        response.setPlanId(plan.getId());

        return response;
    }

}
