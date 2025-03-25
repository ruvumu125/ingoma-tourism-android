package com.ingoma.tourism.api;

import com.ingoma.tourism.model.AdvertisementResponse;
import com.ingoma.tourism.model.AmenityResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AdvertisementApiService {

    @GET("api/v1/advertisements/active")  // Replace with your actual API endpoint
    Call<AdvertisementResponse> getAdvertisements();

}
