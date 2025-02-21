package com.ingoma.tourism.model;

import java.util.List;

public class Room {
    private String name;
    private String imageUrl;
    private int numberOfPictures;
    private int capacity;
    private String bedType;
    private double surface;
    private String amenities;
    private List<Plan> plans;

    public Room(String name, String imageUrl, int numberOfPictures, int capacity,
                String bedType, double surface, String amenities, List<Plan> plans) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.numberOfPictures = numberOfPictures;
        this.capacity = capacity;
        this.bedType = bedType;
        this.surface = surface;
        this.amenities = amenities;
        this.plans = plans;
    }

    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }
    public int getNumberOfPictures() { return numberOfPictures; }
    public int getCapacity() { return capacity; }
    public String getBedType() { return bedType; }
    public double getSurface() { return surface; }
    public String getAmenities() { return amenities; }
    public List<Plan> getPlans() { return plans; }
}

