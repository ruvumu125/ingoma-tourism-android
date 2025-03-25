package com.ingoma.tourism.model;

import com.google.gson.annotations.SerializedName;

public class BookingDetails {
    @SerializedName("booking_id")
    private int bookingId;

    @SerializedName("booking_number")
    private String bookingNumber;

    @SerializedName("booking_date")
    private String bookingDate;

    @SerializedName("check_in_date")
    private String checkInDate;

    @SerializedName("check_out_date")
    private String checkOutDate;

    @SerializedName("duration")
    private String duration;

    @SerializedName("unit_price")
    private String unitPrice;

    @SerializedName("total_price")
    private String totalPrice;

    @SerializedName("currency")
    private String currency;

    @SerializedName("pricing_type")
    private String pricingType;

    @SerializedName("booking_type")
    private String bookingType;

    @SerializedName("status")
    private String status;

    @SerializedName("adults")
    private int adults;

    @SerializedName("children")
    private int children;

    @SerializedName("user")
    private User user;

    @SerializedName("property")
    private BookingDetailsProperty property;

    @SerializedName("guestDetail")
    private BookingDetailsGuestDetails guestDetail;

    @SerializedName("room")
    private BookingDetailsRoom room;

    public int getBookingId() {
        return bookingId;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public String getDuration() {
        return duration;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public String getPricingType() {
        return pricingType;
    }

    public String getBookingType() {
        return bookingType;
    }

    public String getStatus() {
        return status;
    }

    public int getAdults() {
        return adults;
    }

    public int getChildren() {
        return children;
    }

    public User getUser() {
        return user;
    }

    public BookingDetailsProperty getProperty() {
        return property;
    }

    public BookingDetailsRoom getRoom() {
        return room;
    }

    public BookingDetailsGuestDetails getGuestDetail() {
        return guestDetail;
    }
}

