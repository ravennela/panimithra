package com.example.fixmate.dtos.response;

import java.time.LocalTime;
import java.util.List;

import com.example.fixmate.entities.Review;

public class ServiceByIdResponse {

    private String serviceId;
    private String serviceName;
    private String description;
    private double avaragerating;
    private double price;
    private String employeeName;
    private String employeeId;
    private String addInfoOne;
    private String categoryId;
    private List<String> availableDates;
    private int employeeExperiance;
    private String imageUrl;
    private List<Review> reviews;
    private String categoryName;
    private String address;
    private String iconUrl;
    private LocalTime timeIn;
    private int duration;

    public LocalTime getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(LocalTime timeIn) {
        this.timeIn = timeIn;
    }

    private LocalTime timeOut;

    public LocalTime getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(LocalTime timeOut) {
        this.timeOut = timeOut;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    private String subCategoryId;

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String startTime;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    private String endTime;

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public List<String> getAvailableDates() {
        return availableDates;
    }

    public void setAvailableDates(List<String> availableDates) {
        this.availableDates = availableDates;
    }

    private long totalReviewCount;

    public long getTotalReviewCount() {
        return totalReviewCount;
    }

    public void setTotalReviewCount(long totalReviewCount) {
        this.totalReviewCount = totalReviewCount;
    }

    public String getAddInfoOne() {
        return addInfoOne;
    }

    public void setAddInfoOne(String addInfoOne) {
        this.addInfoOne = addInfoOne;
    }

    private String addInfoTwo;

    public String getAddInfoTwo() {
        return addInfoTwo;
    }

    public void setAddInfoTwo(String addInfoTwo) {
        this.addInfoTwo = addInfoTwo;
    }

    private String addInfoThree;

    public String getAddInfoThree() {
        return addInfoThree;
    }

    public void setAddInfoThree(String addInfoThree) {
        this.addInfoThree = addInfoThree;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    private String subCategoryName;

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAvaragerating() {
        return avaragerating;
    }

    public void setAvaragerating(double avaragerating) {
        this.avaragerating = avaragerating;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public int getEmployeeExperiance() {
        return employeeExperiance;
    }

    public void setEmployeeExperiance(int employeeExperiance) {
        this.employeeExperiance = employeeExperiance;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

}
