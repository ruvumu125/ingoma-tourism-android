package com.ingoma.tourism.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RoomImage implements Parcelable {

    private int id;
    private int room_type_id;
    private String image_url;
    private int is_main;

    public int getId() { return id; }
    public int getRoomTypeId() { return room_type_id; }
    public String getImageUrl() { return image_url; }
    public int getIsMain() { return is_main; }

    protected RoomImage(Parcel in) {
        id = in.readInt();
        room_type_id = in.readInt();
        image_url = in.readString();
        is_main = in.readInt();
    }

    public static final Creator<RoomImage> CREATOR = new Creator<RoomImage>() {
        @Override
        public RoomImage createFromParcel(Parcel in) {
            return new RoomImage(in);
        }

        @Override
        public RoomImage[] newArray(int size) {
            return new RoomImage[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(room_type_id);
        dest.writeString(image_url);
        dest.writeInt(is_main);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}

