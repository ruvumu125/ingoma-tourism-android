package com.ingoma.tourism.model;

public class RoomImage {

    private int id;
    private int room_type_id;
    private String image_url;
    private int is_main;

    public int getId() { return id; }
    public int getRoomTypeId() { return room_type_id; }
    public String getImageUrl() { return image_url; }
    public int getIsMain() { return is_main; }
}
