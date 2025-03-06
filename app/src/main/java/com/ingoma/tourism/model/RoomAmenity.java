package com.ingoma.tourism.model;

public class RoomAmenity {

    private int id;
    private String name;
    private int amenity_category_id;
    private String description;
    private String created_at;
    private String updated_at;

    public int getId() { return id; }
    public String getName() { return name; }
    public int getAmenityCategoryId() { return amenity_category_id; }
    public String getDescription() { return description; }
    public String getCreatedAt() { return created_at; }
    public String getUpdatedAt() { return updated_at; }
}
