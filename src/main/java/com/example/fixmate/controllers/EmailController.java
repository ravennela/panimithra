package com.example.fixmate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.fixmate.dtos.custom.ApiErrorDto;
import com.example.fixmate.dtos.request.RequestOtp;
import com.example.fixmate.dtos.request.VerifyOtpRequest;
import com.example.fixmate.dtos.response.OtpResponse;
import com.example.fixmate.dtos.response.VerifyOtpResponse;
import com.example.fixmate.service.EmailService;
import com.example.fixmate.service.OtpService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth/email")
public class EmailController {

    private static final Logger log
            = LoggerFactory.getLogger(OtpService.class);

    @Autowired
    private EmailService emailService;

    @Autowired
    OtpService otpService;

    @PostMapping("/send")
    public String sendMail(@RequestParam String email) {

        emailService.sendEmail(
                email,
                "Test Email",
                "Hello! This email is sent from Spring Boot."
        );

        return "Email sent successfully";
    }

    @PostMapping("/generate-otp")
    public ResponseEntity<?> generateOtp(@RequestBody RequestOtp requestBody) {
        try {
            OtpResponse response = otpService.generateOtp(requestBody);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException exception) {

            log.error("OTP validation failed for email={}",
                    requestBody.getEmail(), exception);

            ApiErrorDto response = new ApiErrorDto();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setError(exception.getMessage());

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);

        } catch (Exception exception) {

            log.error("OTP generation failed (SMTP/unknown) for email={}",
                    requestBody.getEmail(), exception);

            ApiErrorDto response = new ApiErrorDto();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setError("Failed to send OTP email");

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    @PostMapping("/validate-otp")
    public ResponseEntity<?> validateOtp(@RequestBody VerifyOtpRequest request) {
        try {
            VerifyOtpResponse response = otpService.validate(request);
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
}
