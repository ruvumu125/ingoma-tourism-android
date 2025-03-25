package com.ingoma.tourism.model;

import java.util.List;

public class AdvertisementResponse {

    private boolean success;
    private List<Advertisement> data;
    private String message;

    public List<Advertisement> getData() {
        return data;
    }
}
