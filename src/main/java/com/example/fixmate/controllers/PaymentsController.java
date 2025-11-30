package com.example.fixmate.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/webhook")
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
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<?> createCheckout(@RequestParam String userid, @RequestParam String planId) {
        try {
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

        // Check if payload or signature is null
        if (payload == null || signature == null) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Missing payload or signature");
        }

        // 1. Verify webhook signature
        System.out.println("🔍 Verifying signature...");
        if (!verifySignature(payload, signature, webhookSecret)) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }
        System.out.println("✅ Signature verified successfully");

        // 2. Parse JSON payload
        try {
            System.out.println("📝 Parsing JSON payload...");
            JSONObject json = new JSONObject(payload);
            String event = json.getString("event");
            System.out.println("📋 Event Type: " + event);

            if ("payment.captured".equals(event)) {
                System.out.println("💰 Processing payment.captured event...");

                JSONObject paymentEntity = json.getJSONObject("payload")
                        .getJSONObject("payment")
                        .getJSONObject("entity");

                String orderId = paymentEntity.getString("order_id");
                String paymentId = paymentEntity.getString("id");
                String method = paymentEntity.getString("method");

                // 3. Lookup order by Razorpay Order ID
                System.out.println("🔍 Looking up order in database...");
                Orders order = orderRepository.findByRazorpayOrderId(orderId).orElse(null);

                if (order == null) {
                    System.err.println("❌ ERROR: Order not found in database!");
                    System.err.println("   Razorpay Order ID: " + orderId);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
                }

                System.out.println("✅ Order found: " + order.getId());

                Subscription subscription = order.getSubscription();
                if (subscription == null) {
                    System.err.println("❌ ERROR: No subscription linked to order!");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("No subscription found");
                }
                long days = subscription.getSubscriptionPlan().getDurationInDays();

                // Update Order
                System.out.println("💾 Updating order status to PAID...");
                order.setStatus("PAID");
                order.setRazorpayPaymentId(paymentId);
                order.setUpdatedAt(LocalDateTime.now());
                orderRepository.save(order);
                System.out.println("✅ Order updated successfully");

                // Calculate new end date
                LocalDate today = LocalDate.now();
                LocalDate currentEndDate = subscription.getEndDate();
                LocalDate newEndDate;

                if (currentEndDate == null || currentEndDate.isBefore(today)) {
                    // Expired or new user → Start from today
                    newEndDate = today.plusDays(days);
                    subscription.setStartDate(today);

                } else {
                    // Active subscription → Extend from current end date
                    newEndDate = currentEndDate.plusDays(days);
                    System.out.println("   Active subscription - extending from current end date");
                }

                System.out.println("   New End Date: " + newEndDate);

                // Update Subscription
                System.out.println("💾 Updating subscription...");
                subscription.setEndDate(newEndDate);
                subscription.setStatus("ACTIVE");
                subscription.setRazorpayOrderId(orderId);
                subscription.setPaymentId(paymentId);
                subscription.setPaymentMethod(method);
                userSubScriptionRepository.save(subscription);
                System.out.println("✅ Subscription updated successfully!");
                System.out.println("=====================================================");
                System.out.println("🎉 WEBHOOK PROCESSED SUCCESSFULLY");
                System.out.println("=====================================================");
            } else {
                System.out.println("ℹ️ Event type '" + event + "' - No action taken");
            }

        } catch (Exception e) {
            System.err.println("❌ ERROR PROCESSING WEBHOOK!");
            System.err.println("   Error Type: " + e.getClass().getName());
            System.err.println("   Error Message: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing webhook: " + e.getMessage());
        }

        return ResponseEntity.ok("Webhook received");
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
