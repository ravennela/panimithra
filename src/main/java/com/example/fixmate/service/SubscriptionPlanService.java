package com.example.fixmate.service;

import java.util.ArrayList;
import java.util.List;

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
        plan.setOriginalPrice(request.getOriginalPrice());
        plan.setDiscount(request.getDiscount());
        plan.setStatus("ACTIVE");
        repository.save(plan);
        CreateSubscriptionPlanResponse response = new CreateSubscriptionPlanResponse();
        response.setMessage("Plan Created Succefully");
        response.setPlanId(plan.getId());
        return response;
    }

    public List<SubscriptionPlan> fetchPlan(String status, String role) {
        if ("ADMIN".equalsIgnoreCase(role)) {
            return repository.findAll(); // You must have this repo method
        }

        // For USER / EMPLOYEE / ANY OTHER ROLE → return only ACTIVE plans
        String actStatus = (status == null || status.isEmpty()) ? "ACTIVE" : status;

        return repository.getOnlyActivePlans(actStatus);
    }

    public CreateSubscriptionPlanResponse deletePlan(String planId) {
        SubscriptionPlan plan = repository.findById(planId).orElse(null);
        if (plan == null) {
            throw new RuntimeException("No Plan Found");
        }
        repository.deleteById(planId);
        CreateSubscriptionPlanResponse response = new CreateSubscriptionPlanResponse();
        response.setMessage("Plan Deleted Successfully");
        response.setPlanId(planId);
        return response;
    }

    public SubscriptionPlan getPlanById(String planId) {
        SubscriptionPlan plan = repository.findById(planId).orElse(null);
        if (plan == null) {
            throw new RuntimeException("No Plan Found");
        }
        return plan;
    }

    public CreateSubscriptionPlanResponse updateSubScriptionPlan(String planId, CreateSubscriptionPlanRequest request) {
        SubscriptionPlan plan = repository.findById(planId).orElse(null);
        if (plan == null) {
            throw new RuntimeException("No Plan Found");
        }
        plan.setPlanName(request.getPlanName());
        plan.setDescription(request.getDescription());
        plan.setPrice(request.getPrice());
        plan.setDurationInDays(request.getDurationInDays());
        plan.setOriginalPrice(request.getOriginalPrice());
        plan.setDiscount(request.getDiscount());
        plan.setStatus(request.getStatus());
        repository.save(plan);
        CreateSubscriptionPlanResponse response = new CreateSubscriptionPlanResponse();
        response.setMessage("Plan Updated Succefully");
        response.setPlanId(plan.getId());
        return response;
    }
}
