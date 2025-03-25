package com.ingoma.tourism.model;

import com.google.gson.annotations.SerializedName;

public class BookingList {

    @SerializedName("booking_id")
    private Long bookingId;

    @SerializedName("booking_number")
    private String bookingNumber;

    @SerializedName("status")
    private String status;

    @SerializedName("check_in_date")
    private String checkInDate;

    @SerializedName("check_out_date")
    private String checkOutDate;

    @SerializedName("pricing_type")
    private String pricingType;

    @SerializedName("duration")
    private String duration;

    @SerializedName("property")
    private BookingListProperty property;

    public Long getBookingId() {
        return bookingId;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public String getStatus() {
        return status;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public String getPricingType() {
        return pricingType;
    }

    public String getDuration() {
        return duration;
    }

    public BookingListProperty getProperty() {
        return property;
    }
}
