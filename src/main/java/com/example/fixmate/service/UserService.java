package com.example.fixmate.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.fixmate.dtos.custom.UserSpecifications;
import com.example.fixmate.dtos.request.AuthRequest;
import com.example.fixmate.dtos.request.CreateUserRequest;
import com.example.fixmate.dtos.request.ResetBeforeAuthRequest;
import com.example.fixmate.dtos.request.ResetPasswordRequest;
import com.example.fixmate.dtos.response.CreateSubcategoryResponse;
import com.example.fixmate.dtos.response.CreateUserResponse;
import com.example.fixmate.dtos.response.GetUserResponse;
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
            subscription.setEndDate(LocalDate.now().plusDays(plan.getDurationInDays()));
            subscription.setEmployee(user);
            subscription.setSubscriptionPlan(plan);
            subscriptionRepository.save(subscription);
        }

        CreateUserResponse response = new CreateUserResponse();
        response.setMessage("User Created Successfully");
        response.setUserId(user.getId());
        return response;
    }

    public GetUserResponse login(AuthRequest request) {
        User user = userRepository.findByEmailId(request.getUsername());
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));

        if (!auth.isAuthenticated()) {
            throw new UnAutherizedException("Invalid Credentials");
        }

        String token = jwtUtil.generateToken(request.getUsername(), user.getRole());
        // AuthResponse response = new AuthResponse();
        GetUserResponse response = new GetUserResponse();
        response.setUserId(user.getId());
        response.setToken(token);
        response.setUserName(user.getName());
        response.setContactNumber(user.getContactNumber());
        response.setEmailId(user.getEmailId());
        response.setAddress(user.getAddress());
        response.setLatitude(user.getLatitude());
        response.setLongitude(user.getLongitude());
        response.setProfileImageUrl(user.getProfileImageUrl());
        response.setGender(user.getGender());
        response.setDob(user.getDateOfBirth());
        response.setCity(user.getCity());
        response.setState(user.getState());
        response.setPinCode(user.getPincode());
        response.setRole(user.getRole());
        response.setStatus(user.getStatus());
        response.setAlternateNumber(user.getAlternateNumber());
        response.setPrimaryService(user.getPrimaryService());
        return response;
    }

    public Page<User> fetchUsers(int page, String name, String status, String role, Pageable pageable) {
        Specification<User> spec = UserSpecifications.filter(name, status, role);
        return userRepository.findAll(spec, pageable);
    }

    public User userprofile(String userId) {

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("No User Found With this Details");
        }
        return user;
    }

    public CreateSubcategoryResponse deleteUser(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("No User Found With this Details");
        }
        userRepository.deleteById(userId);
        CreateSubcategoryResponse response = new CreateSubcategoryResponse();
        response.setId(userId);
        response.setMessage("User Deleted Successfully");
        return response;
    }

    public CreateSubcategoryResponse changeUserStatus(String userId, String status) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("No User Found With this Details");
        }

        System.out.println("status in java" + status.toString());
        user.setStatus(status);
        userRepository.save(user);
        System.out.println("user saved");
        CreateSubcategoryResponse response = new CreateSubcategoryResponse();
        response.setId(user.getId());
        System.out.println("user status after " + user.getStatus());
        response.setMessage("User Status Changed Successfully");
        return response;
    }

    public List<HashMap<String, String>> faqService() {
        List<HashMap<String, String>> data = new ArrayList<>();
        HashMap<String, String> faq1 = new HashMap<>();
        faq1.put("question", "How do I create an account?");
        faq1.put("answer", "Sign up using your mobile number.");

        HashMap<String, String> faq2 = new HashMap<>();
        faq2.put("question", "How does cashback work?");
        faq2.put("answer", "Cashback is credited after confirmation.");

        data.add(faq1);
        data.add(faq2);
        return data;
    }

    public CreateSubcategoryResponse changePassword(ResetPasswordRequest request) {
        User user = userRepository.findById(request.getUserId()).orElse(null);
        if (user == null) {
            throw new RuntimeException("No User Found With this Details");
        }

        String currentDbPassString = user.getPassword();
        if (currentDbPassString.equals(request.getCurrentPassword())) {
            user.setPassword(request.getNewPassword());
            userRepository.save(user);
        } else {
            throw new RuntimeException("Incorrect Password");
        }

        CreateSubcategoryResponse response = new CreateSubcategoryResponse();
        response.setId(user.getId());
        response.setMessage("Password Updated Successfully");
        return response;
    }

    public CreateSubcategoryResponse resetBeforeAuth(ResetBeforeAuthRequest request) {
        User user = userRepository.findByEmailId(request.getEmailId());
        if (user == null) {
            throw new RuntimeException("No Email Found With this Details");
        }
        String currentDbPassString = user.getPassword();
        if (currentDbPassString.equals(request.getCurrentPassword())) {
            user.setPassword(request.getNewPassword());
            userRepository.save(user);
        } else {
            throw new RuntimeException("Incorrect Password");
        }
        CreateSubcategoryResponse response = new CreateSubcategoryResponse();
        response.setId(user.getId());
        response.setMessage("Password Updated Successfully");
        return response;
    }
}
