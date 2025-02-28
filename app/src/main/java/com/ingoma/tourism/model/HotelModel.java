package com.ingoma.tourism.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class HotelModel implements Parcelable {
    private String name, type, address;
    private double price, rating;
    private List<String> imageUrls;
    private List<String> amenities;

    public HotelModel(String name, String type, String address, double price, double rating,
                      List<String> imageUrls, List<String> amenities) {
        this.name = name;
        this.type = type;
        this.address = address;
        this.price = price;
        this.rating = rating;
        this.imageUrls = imageUrls;
        this.amenities = amenities;
    }

    protected HotelModel(Parcel in) {
        name = in.readString();
        type = in.readString();
        address = in.readString();
        price = in.readDouble();
        rating = in.readDouble();
        imageUrls = in.createStringArrayList();
        amenities = in.createStringArrayList();
    }

    public static final Creator<HotelModel> CREATOR = new Creator<HotelModel>() {
        @Override
        public HotelModel createFromParcel(Parcel in) {
            return new HotelModel(in);
        }

        @Override
        public HotelModel[] newArray(int size) {
            return new HotelModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(name);
        parcel.writeString(type);
        parcel.writeString(address);
        parcel.writeDouble(price);
        parcel.writeDouble(rating);
        parcel.writeStringList(imageUrls);
        parcel.writeStringList(amenities);
    }

    public String getName() { return name; }
    public String getType() { return type; }
    public String getAddress() { return address; }
    public double getPrice() { return price; }
    public double getRating() { return rating; }
    public List<String> getImageUrls() { return imageUrls; }
    public List<String> getAmenities() { return amenities; }
}
