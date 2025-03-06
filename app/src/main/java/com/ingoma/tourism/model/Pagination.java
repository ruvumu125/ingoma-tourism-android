package com.ingoma.tourism.model;

public class Pagination {

    private int total;
    private int count;
    private String per_page;
    private int current_page;
    private int total_pages;

    public int getTotal() {
        return total;
    }

    public String getPer_page() {
        return per_page;
    }

    public int getCount() {
        return count;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public int getTotal_pages() {
        return total_pages;
    }
}
