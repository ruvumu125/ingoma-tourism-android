package com.ingoma.tourism.model;

import java.util.List;

public class NotificationResponse {

    private List<Notification> data;
    private Pagination pagination;

    public List<Notification> getData() { return data; }
    public Pagination getPagination() { return pagination; }
}
