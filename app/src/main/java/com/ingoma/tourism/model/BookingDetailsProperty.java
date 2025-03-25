package com.ingoma.tourism.model;

import com.google.gson.annotations.SerializedName;

public class BookingDetailsProperty {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("address")
    private String address;

    @SerializedName("property_type")
    private String propertyType;

    @SerializedName("image_url")
    private String imageUrl;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
