package com.ingoma.tourism.api;

import com.ingoma.tourism.model.LoginRequest;
import com.ingoma.tourism.model.LoginResponse;
import com.ingoma.tourism.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {

    @POST("api/v1/customerRegister")
    Call<Void> register(@Body User user);

    @POST("api/v1/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}
