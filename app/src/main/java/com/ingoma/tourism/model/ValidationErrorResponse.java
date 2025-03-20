package com.ingoma.tourism.model;

import java.util.List;
import java.util.Map;

public class ValidationErrorResponse {
    private boolean success;
    private Map<String, List<String>> data; // Map to hold field errors
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public Map<String, List<String>> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}

