package com.ingoma.tourism.model;

import com.google.gson.annotations.SerializedName;

public class BookingDetailsResponse {

    @SerializedName("booking")
    private BookingDetails booking;

    public BookingDetails getBooking() {
        return booking;
    }

    public void setBooking(BookingDetails booking) {
        this.booking = booking;
    }
}
