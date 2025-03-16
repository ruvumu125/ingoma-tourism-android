package com.ingoma.tourism.model;

import java.util.List;

public class RoomHotel {
    private int id;
    private String type_name;
    private int property_id;
    private String room_size;
    private String bed_type;
    private int max_guests;
    private String description;
    private List<RoomImage> images;
    private List<RoomAmenity> amenities;
    private List<Plan> plans;
    private Plan selectedPlan; // Store selected plan for this room

    public int getId() { return id; }
    public String getTypeName() { return type_name; }
    public int getPropertyId() { return property_id; }
    public String getRoomSize() { return room_size; }
    public String getBedType() { return bed_type; }
    public int getMaxGuests() { return max_guests; }
    public String getDescription() { return description; }
    public List<RoomImage> getImages() { return images; }
    public List<RoomAmenity> getAmenities() { return amenities; }
    public List<Plan> getPlans() { return plans; }

    public Plan getSelectedPlan() {
        return selectedPlan;
    }

    public void setSelectedPlan(Plan selectedPlan) {
        this.selectedPlan = selectedPlan;
    }
}
