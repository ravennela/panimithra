package com.example.fixmate.dtos.response;

public class CityEmployeeCountDTO {

    private String city;
    private Long count;

    public CityEmployeeCountDTO(String city, Long count) {
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
