package com.example.fixmate.dtos.request;

import java.util.List;

import com.example.fixmate.entities.ServiceAvailableDate;
import com.example.fixmate.entities.ServiceImage;

public class CreateServiceRequest {
    private String name;

    private String description;

    private double price;

    private int duration;

    private String status;

    private double latitude;

    private double longitude;

    private List<ServiveAvailableDateRequest> availableDates;

    private List<ServiceImages> images;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<ServiveAvailableDateRequest> getAvailableDates() {
        return availableDates;
    }

    public void setAvailableDates(List<ServiveAvailableDateRequest> availableDates) {
        this.availableDates = availableDates;
    }

    public List<ServiceImages> getImages() {
        return images;
    }

    public void setImages(List<ServiceImages> images) {
        this.images = images;
    }

}
