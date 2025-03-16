package com.ingoma.tourism.model;

public class RoomGuestHouseImage {

    private int id;
    private int room_type_id;
    private String image_url;
    private int is_main;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public int getRoom_type_id() {
        return room_type_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public int getIs_main() {
        return is_main;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
