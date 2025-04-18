package com.ingoma.tourism.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Plan implements Parcelable {

    private int id;
    private int room_type_id;
    private String plan_type;
    private String price;
    private String currency;
    private String description;
    private String created_at;
    private String updated_at;

    public int getId() { return id; }
    public int getRoomTypeId() { return room_type_id; }
    public String getPlanType() { return plan_type; }
    public String getPrice() { return price; }
    public String getCurrency() { return currency; }
    public String getDescription() { return description; }
    public String getCreatedAt() { return created_at; }
    public String getUpdatedAt() { return updated_at; }

    protected Plan(Parcel in) {
        id = in.readInt();
        room_type_id = in.readInt();
        plan_type = in.readString();
        price = in.readString();
        currency = in.readString();
        description = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
    }

    public static final Creator<Plan> CREATOR = new Creator<Plan>() {
        @Override
        public Plan createFromParcel(Parcel in) {
            return new Plan(in);
        }

        @Override
        public Plan[] newArray(int size) {
            return new Plan[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(room_type_id);
        dest.writeString(plan_type);
        dest.writeString(price);
        dest.writeString(currency);
        dest.writeString(description);
        dest.writeString(created_at);
        dest.writeString(updated_at);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}



