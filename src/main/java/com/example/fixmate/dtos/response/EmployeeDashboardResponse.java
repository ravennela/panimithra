package com.example.fixmate.dtos.response;

import java.util.List;

public class EmployeeDashboardResponse {
    private String employeeName;
    private long totalBookings;
    private long bookingsInprogress;
    private long bookingsCompleted;
    private long cancelledBookings;

    public long getCancelledBookings() {
        return cancelledBookings;
    }

    public void setCancelledBookings(long cancelledBookings) {
        this.cancelledBookings = cancelledBookings;
    }

    private long rejectedBookings;

    public long getRejectedBookings() {
        return rejectedBookings;
    }

    public void setRejectedBookings(long rejectedBookings) {
        this.rejectedBookings = rejectedBookings;
    }

    private double revenue;
    List<MonthWiseRevenue> monthWiseRevenue;

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public long getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(long totalBookings) {
        this.totalBookings = totalBookings;
    }

    public long getBookingsInprogress() {
        return bookingsInprogress;
    }

    public void setBookingsInprogress(long bookingsInprogress) {
        this.bookingsInprogress = bookingsInprogress;
    }

    public long getBookingsCompleted() {
        return bookingsCompleted;
    }

    public void setBookingsCompleted(long bookingsCompleted) {
        this.bookingsCompleted = bookingsCompleted;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public List<MonthWiseRevenue> getMonthWiseRevenue() {
        return monthWiseRevenue;
    }

    public void setMonthWiseRevenue(List<MonthWiseRevenue> monthWiseRevenue) {
        this.monthWiseRevenue = monthWiseRevenue;
    }

}
