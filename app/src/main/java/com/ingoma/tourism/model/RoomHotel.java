package com.ingoma.tourism.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class RoomHotel implements Parcelable {
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
    private Plan selectedPlan;

    // Getters and Setters...
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


    protected RoomHotel(Parcel in) {
        id = in.readInt();
        type_name = in.readString();
        property_id = in.readInt();
        room_size = in.readString();
        bed_type = in.readString();
        max_guests = in.readInt();
        description = in.readString();
        images = in.createTypedArrayList(RoomImage.CREATOR);
        amenities = in.createTypedArrayList(RoomAmenity.CREATOR);
        plans = in.createTypedArrayList(Plan.CREATOR);
        selectedPlan = in.readParcelable(Plan.class.getClassLoader());
    }

    public static final Creator<RoomHotel> CREATOR = new Creator<RoomHotel>() {
        @Override
        public RoomHotel createFromParcel(Parcel in) {
            return new RoomHotel(in);
        }

        @Override
        public RoomHotel[] newArray(int size) {
            return new RoomHotel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(type_name);
        parcel.writeInt(property_id);
        parcel.writeString(room_size);
        parcel.writeString(bed_type);
        parcel.writeInt(max_guests);
        parcel.writeString(description);
        parcel.writeTypedList(images);
        parcel.writeTypedList(amenities);
        parcel.writeTypedList(plans);
        parcel.writeParcelable(selectedPlan, i);
    }
}
