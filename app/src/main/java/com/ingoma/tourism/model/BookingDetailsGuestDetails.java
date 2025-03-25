package com.ingoma.tourism.model;

import com.google.gson.annotations.SerializedName;

public class BookingDetailsGuestDetails {

    @SerializedName("id")
    private int id;

    @SerializedName("booking_id")
    private int bookingId;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("phone")
    private String phone;

    @SerializedName("email")
    private String email;

    public int getId() {
        return id;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}
