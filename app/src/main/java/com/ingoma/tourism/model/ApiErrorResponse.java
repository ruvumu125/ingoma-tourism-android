package com.ingoma.tourism.model;

import java.util.List;

public class ApiErrorResponse {

    private boolean success;
    private Data data;
    private String message;

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class Data {
        private String error;
        private List<String> new_password;

        // Getters and Setters
        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public List<String> getNew_password() {
            return new_password;
        }

        public void setNew_password(List<String> new_password) {
            this.new_password = new_password;
        }
    }
}
