package com.ingoma.tourism.model;

import java.util.List;

public class PropertyListResponse {

    private List<PropertyList> data;
    private Pagination pagination;

    public List<PropertyList> getData() {
        return data;
    }

    public Pagination getPagination() {
        return pagination;
    }
}
