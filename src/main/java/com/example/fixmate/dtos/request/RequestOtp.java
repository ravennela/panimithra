package com.example.fixmate.dtos.request;

import java.time.LocalDateTime;

public class RequestOtp {

    private String email;
    private LocalDateTime currTime;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCurrTime() {
        return currTime;
    }

    public void setCurrTime(LocalDateTime currTime) {
        this.currTime = currTime;
    }


}
