package com.ingoma.tourism.model;

import java.io.Serializable;

public class Sort implements Serializable {
    private String name;
    private boolean isSelected;

    public Sort(String name) {
        this.name = name;
        this.isSelected = false;
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
