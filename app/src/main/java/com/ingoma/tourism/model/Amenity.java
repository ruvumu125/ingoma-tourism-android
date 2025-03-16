package com.ingoma.tourism.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Amenity implements Parcelable {
    private int id;
    private String name;
    private boolean selected; // Add this field

    public Amenity(int id, String name) {
        this.id = id;
        this.name = name;
        this.selected = false;
    }

    protected Amenity(Parcel in) {
        id = in.readInt();
        name = in.readString();
        selected = in.readByte() != 0;
    }

    public static final Creator<Amenity> CREATOR = new Creator<Amenity>() {
        @Override
        public Amenity createFromParcel(Parcel in) {
            return new Amenity(in);
        }

        @Override
        public Amenity[] newArray(int size) {
            return new Amenity[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeByte((byte) (selected ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
