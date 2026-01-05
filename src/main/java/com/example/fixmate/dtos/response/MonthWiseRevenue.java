package com.example.fixmate.dtos.response;

public class MonthWiseRevenue {
    private double amount;
    private String monthName;
    private int month;
    private double totalAmount;

    public MonthWiseRevenue(Integer month, Double totalAmount) {
        this.month = (month != null) ? month : 0;
        this.totalAmount = (totalAmount != null) ? totalAmount : 0.0;
        this.amount = this.totalAmount;
        this.monthName = getMonthName(this.month);
    }

    private String getMonthName(int month) {
        String[] months = {
                "", "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        return (month >= 1 && month <= 12) ? months[month] : "Unknown";
    }

    public MonthWiseRevenue() {
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

}
