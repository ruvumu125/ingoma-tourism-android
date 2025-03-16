package com.ingoma.tourism.model;

import java.util.List;

public class RoomGuestHouse {

    private int id;
    private String type_name;
    private int property_id;
    private String room_size;
    private String bed_type;
    private int max_guests;
    private String description;
    private String created_at;
    private String updated_at;
    private List<RoomGuestHouseImage> images;
    private List<RoomAmenity> amenities;

    public int getId() {
        return id;
    }

    public String getType_name() {
        return type_name;
    }

    public int getProperty_id() {
        return property_id;
    }

    public String getRoom_size() {
        return room_size;
    }

    public String getBed_type() {
        return bed_type;
    }

    public String getDescription() {
        return description;
    }

    public int getMax_guests() {
        return max_guests;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public List<RoomGuestHouseImage> getImages() {
        return images;
    }

    public List<RoomAmenity> getAmenities() {
        return amenities;
    }
}
