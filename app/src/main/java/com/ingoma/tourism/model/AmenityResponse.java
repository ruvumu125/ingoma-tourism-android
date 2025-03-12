package com.ingoma.tourism.model;

import java.util.List;

public class AmenityResponse {

    private String status;
    private List<Amenity> data;

    public String getStatus() {
        return status;
    }

    public List<Amenity> getData() {
        return data;
    }
}
