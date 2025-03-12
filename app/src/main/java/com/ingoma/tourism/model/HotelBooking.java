package com.ingoma.tourism.model;

public class HotelBooking {
    private String hotelName;
    private String checkInDate;
    private String checkOutDate;
    private int numberOfNights;
    private int numberOfGuests;

    public HotelBooking(String hotelName, String checkInDate, String checkOutDate, int numberOfNights, int numberOfGuests) {
        this.hotelName = hotelName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfNights = numberOfNights;
        this.numberOfGuests = numberOfGuests;
    }

    public String getHotelName() { return hotelName; }
    public String getCheckInDate() { return checkInDate; }
    public String getCheckOutDate() { return checkOutDate; }
    public int getNumberOfNights() { return numberOfNights; }
    public int getNumberOfGuests() { return numberOfGuests; }
}

