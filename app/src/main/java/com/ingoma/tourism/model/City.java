package com.ingoma.tourism.model;

public class City {
    private String name;
    private int propertiesCount;
    private int imageResId; // Drawable resource ID

    public City(String name, int propertiesCount, int imageResId) {
        this.name = name;
        this.propertiesCount = propertiesCount;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public int getPropertiesCount() {
        return propertiesCount;
    }

    public int getImageResId() {
        return imageResId;
    }
}
