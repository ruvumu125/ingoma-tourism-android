package com.ingoma.tourism.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private Long id;
    private String first_name;
    private String last_name;
    private String email;
    private String phone_number;
    private String password;
    private String confirm_password;
    private String role;
    private String status;

    // Constructor
    public User(Long id, String first_name, String last_name, String email, String phone_number, String password, String confirm_password, String role, String status) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone_number = phone_number;
        this.password = password;
        this.confirm_password = confirm_password;
        this.role = role;
        this.status = status;
    }

    public User(String last_name, String first_name, String email, String phone_number, String password, String confirm_password) {
        this.last_name = last_name;
        this.first_name = first_name;
        this.email = email;
        this.phone_number = phone_number;
        this.password = password;
        this.confirm_password = confirm_password;
    }

    // Parcelable constructor
    protected User(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        first_name = in.readString();
        last_name = in.readString();
        email = in.readString();
        phone_number = in.readString();
        password = in.readString();
        confirm_password = in.readString();
        role = in.readString();
        status = in.readString();
    }

    // Parcelable Creator
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Parcelable methods
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(id);
        }
        parcel.writeString(first_name);
        parcel.writeString(last_name);
        parcel.writeString(email);
        parcel.writeString(phone_number);
        parcel.writeString(password);
        parcel.writeString(confirm_password);
        parcel.writeString(role);
        parcel.writeString(status);
    }
}

