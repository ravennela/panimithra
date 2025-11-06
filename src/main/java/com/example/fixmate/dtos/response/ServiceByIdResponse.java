package com.example.fixmate.dtos.response;

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

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    private int employeeExperiance;
    private String imageUrl;
    private List<Review> reviews;

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

}
