package com.example.fixmate.dtos.response;

import java.util.List;

public class AdminDashboardResponse {

    private long currentMonthUsers;
    private long previousMonthuser;
    private long currentMonthEmployees;
    private long previousMonthEmployees;
    private long totalBookingsCompleted;
    private long totalBookingsPending;
    private long totalBookinsCancelled;
    private double revenue;
    private long totalBookings;
    private long pendingBookings;
    private long completedBookings;
    private long cancelledBookings;
    private long rejectedBookings;
    private long inporgressBookings;
    private long completedBookingsByPreviousMonth;

    public long getCompletedBookingsByPreviousMonth() {
        return completedBookingsByPreviousMonth;
    }

    public void setCompletedBookingsByPreviousMonth(long completedBookingsByPreviousMonth) {
        this.completedBookingsByPreviousMonth = completedBookingsByPreviousMonth;
    }

    private long pendingdBookingsByPreviouMonth;

    public long getPendingdBookingsByPreviouMonth() {
        return pendingdBookingsByPreviouMonth;
    }

    public void setPendingdBookingsByPreviouMonth(long pendingdBookingsByPreviouMonth) {
        this.pendingdBookingsByPreviouMonth = pendingdBookingsByPreviouMonth;
    }

    private double previousMonthRevenue;

    public void setPreviousMonthRevenue(double previousMonthRevenue) {
        this.previousMonthRevenue = previousMonthRevenue;
    }

    private List<CityBookingCountDTO> cityBookings;
    private List<CityEmployeeCountDTO> cityEmployee;

    public long getTotalBookinsCancelled() {
        return totalBookinsCancelled;
    }

    public void setTotalBookinsCancelled(long totalBookinsCancelled) {
        this.totalBookinsCancelled = totalBookinsCancelled;
    }

    private long totalBookingsRejected;

    public long getTotalBookingsRejected() {
        return totalBookingsRejected;
    }

    public void setTotalBookingsRejected(long totalBookingsRejected) {
        this.totalBookingsRejected = totalBookingsRejected;
    }

    public long getCurrentMonthUsers() {
        return currentMonthUsers;
    }

    public void setCurrentMonthUsers(long currentMonthUsers) {
        this.currentMonthUsers = currentMonthUsers;
    }

    public long getPreviousMonthuser() {
        return previousMonthuser;
    }

    public void setPreviousMonthuser(long previousMonthuser) {
        this.previousMonthuser = previousMonthuser;
    }

    public long getCurrentMonthEmployees() {
        return currentMonthEmployees;
    }

    public void setCurrentMonthEmployees(long currentMonthEmployees) {
        this.currentMonthEmployees = currentMonthEmployees;
    }

    public long getPreviousMonthEmployees() {
        return previousMonthEmployees;
    }

    public void setPreviousMonthEmployees(long previousMonthEmployees) {
        this.previousMonthEmployees = previousMonthEmployees;
    }

    public long getTotalBookingsCompleted() {
        return totalBookingsCompleted;
    }

    public void setTotalBookingsCompleted(long totalBookingsCompleted) {
        this.totalBookingsCompleted = totalBookingsCompleted;
    }

    public long getTotalBookingsPending() {
        return totalBookingsPending;
    }

    public void setTotalBookingsPending(long totalBookingsPending) {
        this.totalBookingsPending = totalBookingsPending;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public long getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(long totalBookings) {
        this.totalBookings = totalBookings;
    }

    public long getPendingBookings() {
        return pendingBookings;
    }

    public void setPendingBookings(long pendingBookings) {
        this.pendingBookings = pendingBookings;
    }

    public long getCompletedBookings() {
        return completedBookings;
    }

    public void setCompletedBookings(long completedBookings) {
        this.completedBookings = completedBookings;
    }

    public long getCancelledBookings() {
        return cancelledBookings;
    }

    public void setCancelledBookings(long cancelledBookings) {
        this.cancelledBookings = cancelledBookings;
    }

    public long getRejectedBookings() {
        return rejectedBookings;
    }

    public void setRejectedBookings(long rejectedBookings) {
        this.rejectedBookings = rejectedBookings;
    }

    public long getInporgressBookings() {
        return inporgressBookings;
    }

    public void setInporgressBookings(long inporgressBookings) {
        this.inporgressBookings = inporgressBookings;
    }

    public List<CityBookingCountDTO> getCityBookings() {
        return cityBookings;
    }

    public void setCityBookings(List<CityBookingCountDTO> cityBookings) {
        this.cityBookings = cityBookings;
    }

    public List<CityEmployeeCountDTO> getCityEmployee() {
        return cityEmployee;
    }

    public void setCityEmployee(List<CityEmployeeCountDTO> cityEmployee) {
        this.cityEmployee = cityEmployee;
    }

}
