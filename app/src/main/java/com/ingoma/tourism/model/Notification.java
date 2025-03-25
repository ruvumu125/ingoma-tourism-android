package com.ingoma.tourism.model;

import com.google.gson.annotations.SerializedName;

public class Notification {
    private int id;
    @SerializedName("user_id")
    private int userId;
    private String type;
    private String message;
    @SerializedName("notification_date")
    private String notificationDate;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;

    // Getters
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getType() { return type; }
    public String getMessage() { return message; }
    public String getNotificationDate() { return notificationDate; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
}


