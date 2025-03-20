package com.ingoma.tourism.model;

import java.util.List;

public class BookingListResponse {

    private List<BookingList> data;
    private Pagination pagination;

    public List<BookingList> getData() {
        return data;
    }

    public Pagination getPagination() {
        return pagination;
    }
}
