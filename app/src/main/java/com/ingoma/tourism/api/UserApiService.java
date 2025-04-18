package com.ingoma.tourism.api;

import com.ingoma.tourism.model.User;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserApiService {

    @GET("api/v1/me")
    Call<User> getCurrentUser();
}
