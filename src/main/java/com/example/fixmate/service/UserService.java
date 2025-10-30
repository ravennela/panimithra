package com.example.fixmate.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.fixmate.dtos.request.AuthRequest;
import com.example.fixmate.dtos.request.CreateUserRequest;
import com.example.fixmate.dtos.response.AuthResponse;
import com.example.fixmate.dtos.response.CreateUserResponse;
import com.example.fixmate.entities.Subscription;
import com.example.fixmate.entities.SubscriptionPlan;
import com.example.fixmate.entities.User;
import com.example.fixmate.repositories.SubscriptionPlanRepository;
import com.example.fixmate.repositories.SubscriptionRepository;
import com.example.fixmate.repositories.UserRepository;
import com.example.fixmate.utils.JwtUtil;
import com.example.fixmate.utils.exceptions.UnAutherizedException;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    SubscriptionPlanRepository subscriptionPlanRepository;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) {
        User user2 = userRepository.findByEmailId(request.getEmailId());
        if (user2 != null) {
            throw new RuntimeException("Email Id Already Exist");
        }
        User user1 = userRepository.findByContactNumber(request.getContactNumber());

        if (user1 != null) {
            throw new RuntimeException("Contact Number Already Exist");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmailId(request.getEmailId());
        user.setContactNumber(request.getContactNumber());
        user.setPassword(request.getPassword());
        user.setAddress(request.getAddress());
        user.setLatitude(request.getLatitude());
        user.setLongitude(request.getLongitude());
        user.setProfileImageUrl(request.getProfileImageUrl());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setGender(request.getGender());
        user.setCity(request.getCity());
        user.setState(request.getState());
        user.setPincode(request.getPincode());
        user.setRole(request.getRole());
        user.setStatus(request.getStatus());
        user.setDeviceToken(request.getDeviceToken());
        user.setAlternateNumber(request.getAlternateMobileNumber());
        user.setPrimaryService(request.getPrimaryService());
        user.setExperiance(request.getExperiance());
        user.setShortBio(request.getShortBio());
        userRepository.save(user);

        if ("EMPLOYEE".equals(request.getRole())) {

            List<SubscriptionPlan> allPlans = subscriptionPlanRepository.findAll();
            System.out.println("All plans: " + allPlans.size());
            for (SubscriptionPlan p : allPlans) {
                System.out.println("Plan in DB: " + p.getPlanName());
            }

            SubscriptionPlan plan = subscriptionPlanRepository.findByPlanNameIgnoreCase("Free Plan");
            System.out.println("plan name" + plan.getPlanName());
            Subscription subscription = new Subscription();
            subscription.setStatus("ACTIVE");
            subscription.setStartDate(LocalDate.now());
            subscription.setEndDate(LocalDate.now().plusDays(365));
            subscription.setEmployee(user);
            subscription.setSubscriptionPlan(plan);
            subscriptionRepository.save(subscription);
        }

        CreateUserResponse response = new CreateUserResponse();
        response.setMessage("User Created Successfully");
        response.setUserId(user.getId());
        return response;
    }

    public AuthResponse login(AuthRequest request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));

        if (!auth.isAuthenticated()) {
            throw new UnAutherizedException("Invalid Credentials");
        }

        User user = userRepository.findByEmailId(request.getUsername());
        String token = jwtUtil.generateToken(request.getUsername(), user.getRole());
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setUser(user);
        return response;
    }

}
