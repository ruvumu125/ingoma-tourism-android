package com.ingoma.tourism.model;

public class Plan {
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

    public String getDescription() {
        return description;
    }

    public String getCreatedAt() { return created_at; }
    public String getUpdatedAt() { return updated_at; }
}


