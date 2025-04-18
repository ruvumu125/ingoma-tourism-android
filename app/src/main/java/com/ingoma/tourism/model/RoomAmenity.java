package com.ingoma.tourism.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RoomAmenity implements Parcelable {

    private int id;
    private String name;
    private int amenity_category_id;
    private String description;
    private String created_at;
    private String updated_at;

    public int getId() { return id; }
    public String getName() { return name; }
    public int getAmenityCategoryId() { return amenity_category_id; }
    public String getDescription() { return description; }
    public String getCreatedAt() { return created_at; }
    public String getUpdatedAt() { return updated_at; }

    protected RoomAmenity(Parcel in) {
        id = in.readInt();
        name = in.readString();
        amenity_category_id = in.readInt();
        description = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
    }

    public static final Creator<RoomAmenity> CREATOR = new Creator<RoomAmenity>() {
        @Override
        public RoomAmenity createFromParcel(Parcel in) {
            return new RoomAmenity(in);
        }

        @Override
        public RoomAmenity[] newArray(int size) {
            return new RoomAmenity[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(amenity_category_id);
        dest.writeString(description);
        dest.writeString(created_at);
        dest.writeString(updated_at);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}

