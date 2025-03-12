package com.ingoma.tourism.model;

import com.google.gson.annotations.SerializedName;

public class Amenity {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
}

