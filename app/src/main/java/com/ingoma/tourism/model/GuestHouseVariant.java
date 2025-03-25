package com.ingoma.tourism.model;

public class GuestHouseVariant {
    private int id;
    private String variant;
    private double price;
    private String currency;
    private String tarification_type;

    public int getId() {
        return id;
    }

    public String getVariant() {
        return variant;
    }

    public double getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public String getTarification_type() {
        return tarification_type;
    }
}
