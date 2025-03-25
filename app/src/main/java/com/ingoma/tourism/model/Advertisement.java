package com.ingoma.tourism.model;

import com.google.gson.annotations.SerializedName;

public class Advertisement {
    private int id;

    @SerializedName("company_name")
    private String companyName;

    @SerializedName("advertisement_url")
    private String advertisementUrl;

    @SerializedName("file_data")
    private String fileData;

    public String getFileData() {
        return fileData;
    }

    public String getAdvertisementUrl() {
        return advertisementUrl;
    }
}

