package com.example.fixmate.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.fixmate.dtos.request.RequestOtp;
import com.example.fixmate.dtos.request.VerifyOtpRequest;
import com.example.fixmate.dtos.response.OtpResponse;
import com.example.fixmate.dtos.response.VerifyOtpResponse;
import com.example.fixmate.entities.EmailOtp;
import com.example.fixmate.repositories.OtpRepository;
import com.example.fixmate.utils.HelperUtil;

@Service
public class OtpService {

    @Autowired
    OtpRepository otpRepository;
    @Autowired
    private EmailService emailService;

    public OtpResponse generateOtp(RequestOtp requestOtp) {
        HelperUtil helperUtil = new HelperUtil();
        String randomString = helperUtil.generateString();
        EmailOtp otp;

        otp = otpRepository.findByEmail(requestOtp.getEmail()).orElse(null);
        if (otp == null) {
            otp = new EmailOtp();
        }
        otp.setEmail(requestOtp.getEmail());
        otp.setVerified(false);
        emailService.sendEmail(
                requestOtp.getEmail(),
                "Otp From Panimithra",
                randomString
        );
        otp.setOtp(randomString);
        otp.setExpiryTime(requestOtp.getCurrTime().plusMinutes(5));
        otpRepository.save(otp);
        OtpResponse response = new OtpResponse();
        response.setEmail(requestOtp.getEmail());
        response.setOtp(randomString);
        return response;
    }

    public VerifyOtpResponse validate(VerifyOtpRequest request) {

        EmailOtp emailId = otpRepository.findByEmail(request.getEmailId())
                .orElseThrow(() -> new IllegalArgumentException("Email not found"));
        if (emailId.isVerified()) {
            throw new RuntimeException("OTP already used");
        }
        if (emailId.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Otp Expired");
        }
        if (!request.getOtp().equals(emailId.getOtp())) {
            throw new RuntimeException("Otp Not Matched");
        }
        emailId.setVerified(true);
        otpRepository.save(emailId);
        VerifyOtpResponse response = new VerifyOtpResponse();
        response.setId(emailId.getId());
        response.setMessage("Otp Verified Successfully");
        return response;
    }
}
