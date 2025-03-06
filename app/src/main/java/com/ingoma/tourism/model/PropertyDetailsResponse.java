package com.ingoma.tourism.model;

import com.google.gson.annotations.SerializedName;

public class PropertyDetailsResponse {

    @SerializedName("data")
    private PropertyDetails data;

    public PropertyDetails getData() {
        return data;
    }
}
