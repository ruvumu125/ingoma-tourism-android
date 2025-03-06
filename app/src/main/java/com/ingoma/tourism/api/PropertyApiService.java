package com.ingoma.tourism.api;

import com.ingoma.tourism.model.HotelRoomsResponse;
import com.ingoma.tourism.model.PropertyDetailsResponse;
import com.ingoma.tourism.model.PropertyListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PropertyApiService {

    @GET("api/v1/properties-listing") // Endpoint: /api/properties
    Call<PropertyListResponse> getProperties(
            @Query("property_type") String propertyType,  // Required parameter
            @Query("search") String search,  // Optional search query
            @Query("per_page") int perPage,
            @Query("page") int page
    );

    @GET("api/v1/select-property/{id}")
    Call<PropertyDetailsResponse> getProperty(@Path("id") Long id);

    @GET("api/v1/show-rooms/{property_id}")
    Call<HotelRoomsResponse> getPropertyRooms(@Path("property_id") Long propertyId);
}
