package com.ingoma.tourism.model;

import java.util.List;

public class PropertyList {
    private Long id;
    private String name;
    private String city;
    private String property_type;
    private String hotel_type;
    private String address;
    private double min_price;
    private String currency;
    private List<String> images;

    public Long getId() {
        return id;
    }

    public String getName() { return name; }
    public String getCity() { return city; }
    public String getPropertyType() { return property_type; }
    public String getHotelType() { return hotel_type; }
    public String getAddress() { return address; }
    public double getMinPrice() { return min_price; }
    public String getCurrency() { return currency; }
    public List<String> getImages() { return images; }
}
