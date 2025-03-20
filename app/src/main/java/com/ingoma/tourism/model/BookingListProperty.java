package com.ingoma.tourism.model;

import com.google.gson.annotations.SerializedName;

public class BookingListProperty {

    @SerializedName("property_id")
    private int propertyId;

    @SerializedName("property_name")
    private String propertyName;

    @SerializedName("property_address")
    private String propertyAddress;

    @SerializedName("property_main_image")
    private String propertyMainImage;

    public int getPropertyId() {
        return propertyId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public String getPropertyMainImage() {
        return propertyMainImage;
    }
}
