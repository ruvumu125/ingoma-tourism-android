package com.ingoma.tourism.model;

import com.google.gson.annotations.SerializedName;

public class BookingDetailsRoomPlan {

    @SerializedName("id")
    private int id;

    @SerializedName("plan_type")
    private String planType;

    @SerializedName("price")
    private String price;

    @SerializedName("currency")
    private String currency;

    @SerializedName("description")
    private String description;

    public int getId() {
        return id;
    }

    public String getPlanType() {
        return planType;
    }

    public String getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDescription() {
        return description;
    }
}
