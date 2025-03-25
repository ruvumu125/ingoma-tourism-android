package com.ingoma.tourism.model;

public class Booking {

    private int user_id;
    private int property_id;
    private String check_in_date;
    private String check_out_date;
    private double unit_price;
    private String pricing_type;
    private String duration;
    private double total_price;
    private String currency;
    private String booking_type;
    private String first_name;
    private String last_name;
    private String phone;
    private String email;
    private int adults;
    private int children;
    private int room_id;
    private int room_plan_id;

    public Booking(int user_id, int property_id, String check_in_date, String check_out_date, double unit_price, String pricing_type, String duration, double total_price, String currency, String booking_type, String first_name, String last_name, String phone, String email, int adults, int children, int room_id, int room_plan_id) {
        this.user_id = user_id;
        this.property_id = property_id;
        this.check_in_date = check_in_date;
        this.check_out_date = check_out_date;
        this.unit_price = unit_price;
        this.pricing_type = pricing_type;
        this.duration = duration;
        this.total_price = total_price;
        this.currency = currency;
        this.booking_type = booking_type;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
        this.email = email;
        this.adults = adults;
        this.children = children;
        this.room_id = room_id;
        this.room_plan_id = room_plan_id;
    }

    public Booking(int property_id, String check_in_date, String check_out_date, double unit_price, String pricing_type, String duration, double total_price, String currency, String booking_type, String first_name, String phone, String last_name, String email, int children, int adults, int room_id, int room_plan_id) {
        this.property_id = property_id;
        this.check_in_date = check_in_date;
        this.check_out_date = check_out_date;
        this.unit_price = unit_price;
        this.pricing_type = pricing_type;
        this.duration = duration;
        this.total_price = total_price;
        this.currency = currency;
        this.booking_type = booking_type;
        this.first_name = first_name;
        this.phone = phone;
        this.last_name = last_name;
        this.email = email;
        this.children = children;
        this.adults = adults;
        this.room_id = room_id;
        this.room_plan_id = room_plan_id;
    }



    // Getters and Setters
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getProperty_id() {
        return property_id;
    }

    public void setProperty_id(int property_id) {
        this.property_id = property_id;
    }

    public String getCheck_in_date() {
        return check_in_date;
    }

    public void setCheck_in_date(String check_in_date) {
        this.check_in_date = check_in_date;
    }

    public String getCheck_out_date() {
        return check_out_date;
    }

    public void setCheck_out_date(String check_out_date) {
        this.check_out_date = check_out_date;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }

    public String getPricing_type() {
        return pricing_type;
    }

    public void setPricing_type(String pricing_type) {
        this.pricing_type = pricing_type;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBooking_type() {
        return booking_type;
    }

    public void setBooking_type(String booking_type) {
        this.booking_type = booking_type;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAdults() {
        return adults;
    }

    public void setAdults(int adults) {
        this.adults = adults;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public int getRoom_plan_id() {
        return room_plan_id;
    }

    public void setRoom_plan_id(int room_plan_id) {
        this.room_plan_id = room_plan_id;
    }
}

