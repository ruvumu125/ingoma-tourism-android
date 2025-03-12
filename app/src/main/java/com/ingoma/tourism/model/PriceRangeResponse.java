package com.ingoma.tourism.model;

public class PriceRangeResponse {
    private String property_type;
    private String city_name;
    private double min_price;
    private double max_price;

    // Getters and setters
    public String getPropertyType() {
        return property_type;
    }

    public void setPropertyType(String property_type) {
        this.property_type = property_type;
    }

    public String getCityName() {
        return city_name;
    }

    public void setCityName(String city_name) {
        this.city_name = city_name;
    }

    public double getMinPrice() {
        return min_price;
    }

    public void setMinPrice(double min_price) {
        this.min_price = min_price;
    }

    public double getMaxPrice() {
        return max_price;
    }

    public void setMaxPrice(double max_price) {
        this.max_price = max_price;
    }
}

