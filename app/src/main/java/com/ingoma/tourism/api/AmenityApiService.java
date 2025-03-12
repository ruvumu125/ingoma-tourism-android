package com.ingoma.tourism.api;

import com.ingoma.tourism.model.Amenity;
import com.ingoma.tourism.model.AmenityResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AmenityApiService {

    // Fetch amenities by property type and city ID
    @GET("api/v1/amenities-by-property-type-and-city")
    Call<AmenityResponse> getAmenitiesByCity(
            @Query("property_type") String propertyType,
            @Query("city_name") String cityName
    );

    // Fetch amenities by property type and property name
    @GET("api/v1/amenities-by-property-type-and-propertyname")
    Call<AmenityResponse> getAmenitiesByName(
            @Query("property_type") String propertyType,
            @Query("property_name") String propertyName
    );
}
