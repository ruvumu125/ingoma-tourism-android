package com.ingoma.tourism.model;

import android.os.Parcel;
import android.os.Parcelable;

import android.os.Parcel;
import android.os.Parcelable;

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
    private String created_at;
    private String updated_at;
    private String email_verified_at;
    private String whatsapp_verified_at;
    private String preferred_notification_channel;

    // Constructor
    public User(Long id, String first_name, String last_name, String email, String phone_number,
                String password, String confirm_password, String role, String status,
                String created_at, String updated_at, String email_verified_at,
                String whatsapp_verified_at, String preferred_notification_channel) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone_number = phone_number;
        this.password = password;
        this.confirm_password = confirm_password;
        this.role = role;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.email_verified_at = email_verified_at;
        this.whatsapp_verified_at = whatsapp_verified_at;
        this.preferred_notification_channel = preferred_notification_channel;
    }

    public User(String first_name, String last_name, String email, String password, String confirm_password) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.confirm_password = confirm_password;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirm_password() {
        return confirm_password;
    }

    public String getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public String getWhatsapp_verified_at() {
        return whatsapp_verified_at;
    }

    public String getPreferred_notification_channel() {
        return preferred_notification_channel;
    }

    // Parcelable implementation
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
        created_at = in.readString();
        updated_at = in.readString();
        email_verified_at = in.readString();
        whatsapp_verified_at = in.readString();
        preferred_notification_channel = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(email);
        dest.writeString(phone_number);
        dest.writeString(password);
        dest.writeString(confirm_password);
        dest.writeString(role);
        dest.writeString(status);
        dest.writeString(created_at);
        dest.writeString(updated_at);
        dest.writeString(email_verified_at);
        dest.writeString(whatsapp_verified_at);
        dest.writeString(preferred_notification_channel);
    }

    @Override
    public int describeContents() {
        return 0;
    }

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
}
