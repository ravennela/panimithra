package com.example.fixmate.dtos.request;

public class CreateSubscriptionPlanResponse {

    private String planId;

    private String message;

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
