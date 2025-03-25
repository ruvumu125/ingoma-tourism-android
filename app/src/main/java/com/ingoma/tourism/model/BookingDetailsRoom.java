package com.ingoma.tourism.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookingDetailsRoom {

    @SerializedName("id")
    private int id;

    @SerializedName("type_name")
    private String typeName;

    @SerializedName("room_size")
    private String roomSize;

    @SerializedName("bed_type")
    private String bedType;

    @SerializedName("max_guests")
    private int maxGuests;

    @SerializedName("description")
    private String description;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("amenities")
    private List<BookingDetailsRoomAmenity> amenities;

    @SerializedName("room_plan")
    private BookingDetailsRoomPlan roomPlan;

    public int getId() {
        return id;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getRoomSize() {
        return roomSize;
    }

    public String getBedType() {
        return bedType;
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<BookingDetailsRoomAmenity> getAmenities() {
        return amenities;
    }

    public BookingDetailsRoomPlan getRoomPlan() {
        return roomPlan;
    }
}
