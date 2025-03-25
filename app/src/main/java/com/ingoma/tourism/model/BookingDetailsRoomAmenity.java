package com.ingoma.tourism.model;

import com.google.gson.annotations.SerializedName;

public class BookingDetailsRoomAmenity {

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
