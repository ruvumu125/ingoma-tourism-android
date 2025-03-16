package com.ingoma.tourism.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

// GuestHouse.java
public class GuestHouse {
    private int id;
    private String name;
    private String description;
    private String address;
    private int city_id;
    private Double latitude;
    private Double longitude;
    private String property_type;
    private String whatsapp_number1;
    private String whatsapp_number2;
    private int rating; // Ignore this field
    private int is_active;
    private String created_at;
    private String updated_at;
    private List<RoomGuestHouse> roomtypes;
    private List<GuestHouseVariant> guest_house_variants;

    // Getters and Setters


    public int getIs_active() {
        return is_active;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public int getCity_id() {
        return city_id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getProperty_type() {
        return property_type;
    }

    public String getWhatsapp_number1() {
        return whatsapp_number1;
    }

    public String getWhatsapp_number2() {
        return whatsapp_number2;
    }

    public int getRating() {
        return rating;
    }




    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public List<RoomGuestHouse> getRoomtypes() {
        return roomtypes;
    }

    public List<GuestHouseVariant> getGuest_house_variants() {
        return guest_house_variants;
    }
}

