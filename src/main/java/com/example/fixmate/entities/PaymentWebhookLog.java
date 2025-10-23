package com.example.fixmate.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class PaymentWebhookLog {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "VARCHAR(36)")
    private String id;

    private String eventType; // payment.authorized / payment.failed / order.paid
    private String payload; // full JSON from Razorpay
    private String signature;

    private String status; // PROCESSED / FAILED / IGNORED

    @CreationTimestamp
    private LocalDateTime receivedAt;

}
