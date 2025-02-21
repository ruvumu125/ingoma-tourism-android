package com.ingoma.tourism.model;

public class Hotel {
    private String name;
    private String type;
    private String address;
    private String imageUrl;
    private double rating;
    private double price;

    public Hotel(String name, String type, String address, String imageUrl, double rating, double price) {
        this.name = name;
        this.type = type;
        this.address = address;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getAddress() {
        return address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public double getRating() {
        return rating;
    }

    public double getPrice() {
        return price;
    }
}

