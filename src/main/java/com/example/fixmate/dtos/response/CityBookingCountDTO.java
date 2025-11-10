package com.example.fixmate.dtos.response;

public class CityBookingCountDTO {
    private String city;
    private Long count;

    public CityBookingCountDTO(String city, Long count) {
        this.city = city;
        this.count = count;
    }

    public String getCity() {
        return city;
    }

    public Long getCount() {
        return count;
    }
}
