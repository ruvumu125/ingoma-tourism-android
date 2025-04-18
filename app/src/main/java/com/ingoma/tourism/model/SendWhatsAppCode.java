package com.ingoma.tourism.model;

public class SendWhatsAppCode {
    private String whatsapp_number;

    public SendWhatsAppCode(String whatsapp_number) {
        this.whatsapp_number = whatsapp_number;
    }

    public String getWhatsapp_number() {
        return whatsapp_number;
    }
}
