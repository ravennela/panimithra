package com.example.fixmate.service;

import java.time.LocalDate;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.fixmate.dtos.response.CheckoutResponse;
import com.example.fixmate.entities.Orders;
import com.example.fixmate.entities.Subscription;
import com.example.fixmate.entities.SubscriptionPlan;
import com.example.fixmate.entities.User;
import com.example.fixmate.repositories.OrdersRepository;
import com.example.fixmate.repositories.SubscriptionPlanRepository;
import com.example.fixmate.repositories.SubscriptionRepository;
import com.example.fixmate.repositories.UserRepository;
import com.example.fixmate.utils.exceptions.InvalidRequestException;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Service
public class PaymentsService {
    @Autowired
    SubscriptionPlanRepository subscriptionPlanRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SubscriptionRepository subscriptionRepository;
    @Autowired
    RazorpayService razorpayService;

    @Autowired
    OrdersRepository ordersRepository;

    @Transactional
    public CheckoutResponse createCheckout(String userId, String planid) throws RazorpayException {

        SubscriptionPlan plan = subscriptionPlanRepository.findById(planid).orElse(null);
        if (plan == null) {
            throw new RuntimeException("Invalid Plan Selected");
        }
        double amount = plan.getPrice();
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("Employee Not Found With this Id");
        }
        Subscription subscription;

        // 1️⃣ Check for an active subscription
        Optional<Subscription> activeSubOpt = subscriptionRepository.findActiveByEmployee_Id(userId, LocalDate.now());

        if (activeSubOpt.isPresent()) {
            // Found an active subscription → don’t create a new one
            subscription = activeSubOpt.get();

            // (At this stage, don’t modify dates yet — only extend after payment success)
        } else {
            // 2️⃣ No active subscription → check if there is a pending one
            Subscription lastSub = subscriptionRepository.findTopByEmployee_IdOrderByStartDateDesc(userId);

            if (lastSub != null && "PENDING".equalsIgnoreCase(lastSub.getStatus())) {
                // Reuse existing pending subscription
                subscription = lastSub;
            } else {
                // 3️⃣ No pending subscription → create a fresh one
                subscription = new Subscription();
                subscription.setEmployee(user);
                subscription.setStatus("PENDING");
                subscription.setPrice(plan.getPrice());
                subscription.setSubscriptionPlan(plan);
                subscriptionRepository.save(subscription);
            }
        }

        // 5️⃣ Check if there is already a pending order for this subscription
        Optional<Orders> pendingOrder = ordersRepository.findTopBySubscriptionIdAndStatusOrderByCreatedAtDesc(
                subscription.getId(),
                "PENDING");

        Orders order;
        // if (pendingOrder.isPresent()) {
        //     System.out.println("re -usable order id present" + pendingOrder.get().getId());
        //     order = pendingOrder.get(); // reuse existing pending order
        // }
        // else {
            System.out.println("new razorpay id");
            // 6️⃣ Create new Razorpay order
            String razorOrderId = razorpayService.createOrder((int) amount, "INR", userId);
            order = new Orders();
            order.setSubscription(subscription);
            order.setAmount(amount);
            order.setStatus("PENDING");
            order.setRazorpayOrderId(razorOrderId);
            ordersRepository.save(order);
       // }
        // 7️⃣ Return checkout response
        return new CheckoutResponse(
                order.getRazorpayOrderId(),
                order.getAmount(),
                "INR",
                plan.getId(),
                order.getStatus());

    }
}
