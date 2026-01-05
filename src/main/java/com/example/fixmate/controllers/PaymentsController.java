package com.example.fixmate.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.fixmate.dtos.custom.ApiErrorDto;
import com.example.fixmate.dtos.response.CheckoutResponse;
import com.example.fixmate.entities.Orders;
import com.example.fixmate.entities.Subscription;
import com.example.fixmate.entities.SubscriptionPlan;
import com.example.fixmate.repositories.OrdersRepository;
import com.example.fixmate.repositories.SubscriptionRepository;
import com.example.fixmate.service.PaymentsService;
import com.razorpay.Utils;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/payments")
public class PaymentsController {

    @Autowired
    PaymentsService paymentsService;

    @Value("${razorpay.webhook.secret}")
    private String webhookSecret;

    @Autowired
    private OrdersRepository orderRepository;
    @Autowired
    private SubscriptionRepository userSubScriptionRepository;

    @PostMapping("/checkout")
    public ResponseEntity<?> createCheckout(@RequestParam String userid, @RequestParam String planId) {
        try {
            System.out.println("Reaching Server no issues");
            CheckoutResponse response = paymentsService.createCheckout(userid, planId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException exception) {
            ApiErrorDto response = new ApiErrorDto();
            System.out.println(exception.getStackTrace());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setError(exception.getMessage());
            System.out.println(exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception exception) {
            ApiErrorDto response = new ApiErrorDto();
            System.out.println(exception.getStackTrace());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setError(exception.getMessage());
            System.out.println(exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/pay")
    public ResponseEntity<String> handleWebhook(
            @RequestHeader(value = "X-Razorpay-Signature", required = false) String signature,
            @RequestBody(required = false) String payload) throws Exception {

        // TEMP DEBUG — REMOVE AFTER FIX
        if (webhookSecret == null) {
            System.out.println("❌ WEBHOOK SECRET IS NULL");
        } else {
            System.out.println("WEBHOOK SECRET RAW = [" + webhookSecret + "]");
            System.out.println("✅ WEBHOOK SECRET LOADED, LENGTH = " + webhookSecret.length());
            for (int i = 0; i < webhookSecret.length(); i++) {
                System.out.println(
                        "char[" + i + "] = '" + webhookSecret.charAt(i) + "' ("
                        + (int) webhookSecret.charAt(i) + ")"
                );
            }
        }
        System.out.println("api recieved");

        if (payload == null || signature == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Missing payload or signature");
        }
        System.out.println("signature verified");
        // Verify signature
        if (!verifySignature(payload, signature, webhookSecret.trim())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }
        System.out.println("Captured");
        JSONObject json = new JSONObject(payload);
        String event = json.getString("event");

        if (!event.equals("payment.captured")) {
            return ResponseEntity.ok("Event ignored");
        }

        JSONObject paymentEntity = json.getJSONObject("payload")
                .getJSONObject("payment")
                .getJSONObject("entity");
        String orderId = paymentEntity.getString("order_id");
        String paymentId = paymentEntity.getString("id");
        String method = paymentEntity.getString("method");
        Orders order = orderRepository.findByRazorpayOrderId(orderId).orElse(null);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }
        System.out.println("order status" + order.getStatus());

        // Prevent duplicate webhook calls
        if ("PAID".equalsIgnoreCase(order.getStatus())) {
            System.out.println("already paied");
            return ResponseEntity.ok("Already processed");
        }

        Subscription subscription = order.getSubscription();
        SubscriptionPlan plan = order.getPlan(); // Make sure plan is stored!

        System.out.println("order in verification" + plan.getId() + "days" + plan.getDurationInDays());

        int days = plan.getDurationInDays();

        // Update order
        order.setStatus("PAID");
        order.setRazorpayPaymentId(paymentId);
        order.setPaymentMethod(method);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        // Update subscription
        LocalDate today = LocalDate.now();
        LocalDate currentEnd = subscription.getEndDate();
        LocalDate newEndDate;

        if (currentEnd == null || currentEnd.isBefore(today)) {
            newEndDate = today.plusDays(days);
            subscription.setStartDate(today);
        } else {
            newEndDate = currentEnd.plusDays(days);
        }
        subscription.setEndDate(newEndDate);
        subscription.setStatus("ACTIVE");
        subscription.setSubscriptionPlan(plan);
        userSubScriptionRepository.save(subscription);
        return ResponseEntity.ok("OK");
    }

    private boolean verifySignature(String payload, String signature, String secret) {
        try {
            return Utils.verifyWebhookSignature(payload, signature, secret);
        } catch (Exception e) {
            // Optionally log this error for debugging
            System.err.println("Signature verification failed: " + e.getMessage());
            return false;
        }
    }
}
