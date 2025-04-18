package com.ingoma.tourism.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PropertyImage implements Parcelable {
    private int id;
    private String image_url;
    private int is_main;

    public PropertyImage(int id, String image_url, int is_main) {
        this.id = id;
        this.image_url = image_url;
        this.is_main = is_main;
    }

    // Getter methods
    public int getId() { return id; }
    public String getImageUrl() { return image_url; }
    public int getIsMain() { return is_main; }

    // Parcelable implementation
    protected PropertyImage(Parcel in) {
        id = in.readInt();
        image_url = in.readString();
        is_main = in.readInt();
    }

    public static final Creator<PropertyImage> CREATOR = new Creator<PropertyImage>() {
        @Override
        public PropertyImage createFromParcel(Parcel in) {
            return new PropertyImage(in);
        }

        @Override
        public PropertyImage[] newArray(int size) {
            return new PropertyImage[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(image_url);
        dest.writeInt(is_main);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}

