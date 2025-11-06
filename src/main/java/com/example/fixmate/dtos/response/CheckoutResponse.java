package com.example.fixmate.dtos.response;

public class CheckoutResponse {
    String razorpayOrderId;
    Double amount;
    String currency;
    String subscriptionId; // optional
    String status;

    public CheckoutResponse() {
    }

    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }

    public CheckoutResponse(String razorpayOrderId, Double amount, String currency, String subscriptionId,
            String status) {
        this.razorpayOrderId = razorpayOrderId;
        this.amount = amount;
        this.currency = currency;
        this.subscriptionId = subscriptionId;
        this.status = status;
    }

    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}