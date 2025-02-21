package com.ingoma.tourism.model;

import java.util.List;

public class HotelModel {
    private String name, type, address;
    private double price, rating;
    private List<String> imageUrls;
    private List<String> amenities;

    public HotelModel(String name, String type, String address, double price, double rating,
                      List<String> imageUrls, List<String> amenities) {
        this.name = name;
        this.type = type;
        this.address = address;
        this.price = price;
        this.rating = rating;
        this.imageUrls = imageUrls;
        this.amenities = amenities;
    }

    public String getName() { return name; }
    public String getType() { return type; }
    public String getAddress() { return address; }
    public double getPrice() { return price; }
    public double getRating() { return rating; }
    public List<String> getImageUrls() { return imageUrls; }
    public List<String> getAmenities() { return amenities; }
}
