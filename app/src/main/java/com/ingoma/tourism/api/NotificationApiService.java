package com.ingoma.tourism.api;

import com.ingoma.tourism.model.NotificationResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NotificationApiService {

    @GET("api/v1/notifications/user/{userId}") // Replace with your actual API endpoint
    Call<NotificationResponse> getNotifications(
            @Path("userId") Long userId,
            @Query("per_page") int perPage,
            @Query("page") int page
    );
}
