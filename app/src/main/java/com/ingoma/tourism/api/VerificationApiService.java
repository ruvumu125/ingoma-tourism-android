package com.ingoma.tourism.api;

import com.ingoma.tourism.model.SendEmailCode;
import com.ingoma.tourism.model.SendWhatsAppCode;
import com.ingoma.tourism.model.VerifyEmail;
import com.ingoma.tourism.model.VerifyWhatsApp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface VerificationApiService {

    @POST("api/v1/send-otp-email")
    Call<SendEmailCode> sendEmailCode(@Body SendEmailCode sendEmailCode);

    @POST("api/v1/send-otp-whatsapp")
    Call<SendWhatsAppCode> sendWhatsAppCode(@Body SendWhatsAppCode sendWhatsAppCode);

    @POST("api/v1/verify-email")
    Call<VerifyEmail> verifyEmail(@Body VerifyEmail verifyEmail);

    @POST("api/v1/verify-whatsapp")
    Call<VerifyWhatsApp> verifyWhatsApp(@Body VerifyWhatsApp verifyWhatsApp);
}
