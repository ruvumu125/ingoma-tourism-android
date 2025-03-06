package com.ingoma.tourism.model;

import java.util.List;

public class PropertyDetails {
    private int id;
    private String name;
    private String property_type;
    private String description;
    private String address;
    private Double latitude;
    private Double longitude;
    private City city;
    private HotelType hotel_type;
    private String whatsapp_number1;
    private String whatsapp_number2;
    private int rating;
    private int is_active;
    private List<PropertyAmenity> amenities;
    private List<Landmark> landmarks;
    private List<Rule> rules;
    private List<PropertyImage> images;
    private List<SimilarProperty> similar_properties;

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getPropertyType() { return property_type; }
    public String getDescription() { return description; }
    public String getAddress() { return address; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public City getCity() { return city; }
    public HotelType getHotelType() { return hotel_type; }
    public String getWhatsappNumber1() { return whatsapp_number1; }
    public String getWhatsappNumber2() { return whatsapp_number2; }
    public int getRating() { return rating; }
    public int getIsActive() { return is_active; }
    public List<PropertyAmenity> getAmenities() { return amenities; }
    public List<Landmark> getLandmarks() { return landmarks; }
    public List<Rule> getRules() { return rules; }
    public List<PropertyImage> getImages() { return images; }
    public List<SimilarProperty> getSimilarProperties() { return similar_properties; }
}

