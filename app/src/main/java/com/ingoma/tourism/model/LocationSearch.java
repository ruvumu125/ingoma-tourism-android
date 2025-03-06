package com.ingoma.tourism.model;

import com.google.gson.annotations.SerializedName;

public class LocationSearch {

    @SerializedName("type")
    private String type; // "city" or "property"

    @SerializedName("name")
    private String name;

    @SerializedName("property_count") // Only for cities
    private int propertyCount;

    @SerializedName("address") // Only for properties
    private String address;

    @SerializedName("is_hotel")
    private boolean isHotel;

    // Getters
    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getPropertyCount() {
        return propertyCount;
    }

    public String getAddress() {
        return address;
    }

    public boolean isHotel() {
        return isHotel;
    }
}
