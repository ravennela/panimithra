package com.example.fixmate.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import jakarta.annotation.PostConstruct;

@Service
public class RazorpayService {

    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${razorpay.api.secret}")
    private String apiSecret;

    // 👇 ADD THIS METHOD HERE
    @PostConstruct
    public void debugRazorpay() {
        System.out.println("Razorpay key = " + apiKey);
        System.out.println(
                "Razorpay secret length = " + (apiSecret != null ? apiSecret.length() : 0)
        );
    }

    public String createOrder(int amount, String currency, String recieptId) throws RazorpayException {
        RazorpayClient razorpayClient
                = new RazorpayClient(apiKey.trim(), apiSecret.trim());
        System.out.println("Secret chars:");
        for (char c : apiSecret.toCharArray()) {
            System.out.print((int) c + " ");
        }
        System.out.println();
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount * 100);
        orderRequest.put("currency", currency);
        orderRequest.put("receipt", recieptId);
        Order order = razorpayClient.Orders.create(orderRequest);
        System.out.println(order.toString());
        return order.get("id");
    }
}
