package com.ingoma.tourism.model;

public class VerifyWhatsApp {

    String code;
    String preferred_notification_channel;

    public VerifyWhatsApp(String code, String preferred_notification_channel) {
        this.code = code;
        this.preferred_notification_channel = preferred_notification_channel;
    }

    public String getCode() {
        return code;
    }

    public String getPreferred_notification_channel() {
        return preferred_notification_channel;
    }
}
