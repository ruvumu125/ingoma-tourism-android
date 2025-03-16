package com.ingoma.tourism.api;

import com.ingoma.tourism.model.GuestHouse;
import com.ingoma.tourism.model.HotelRoomsResponse;
import com.ingoma.tourism.model.PropertyDetailsResponse;
import com.ingoma.tourism.model.PropertyListResponse;

import java.util.List;

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

    @GET("api/v1/guesthouse-rooms/{property_id}")
    Call<GuestHouse> getGuestHouseRooms(@Path("property_id") Long propertyId);

    @GET("api/v1/properties/filter")
    Call<PropertyListResponse> listing(
            @Query("property_type") String propertyType,
            @Query("search") String search,
            @Query("sort_by") String sortBy,
            @Query("min_price") Float minPrice,
            @Query("max_price") Float maxPrice,
            @Query("amenities[]") List<Integer> amenities, // Supports multiple amenities
            @Query("per_page") int perPage,
            @Query("page") int page
    );
}
