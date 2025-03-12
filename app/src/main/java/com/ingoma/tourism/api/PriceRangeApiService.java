package com.ingoma.tourism.api;

import com.ingoma.tourism.model.PriceRangeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PriceRangeApiService {

    @GET("api/v1/price-range-by-property-type-and-city") // Replace with your actual endpoint
    Call<PriceRangeResponse> getPriceRangeByTypeAndCityName(
            @Query("property_type") String propertyType,
            @Query("city_name") String cityName
    );

    @GET("api/v1/price-range-by-property-type-and-propertyname") // Replace with your actual endpoint
    Call<PriceRangeResponse> getPriceRangeByTypeAndPropertyName(
            @Query("property_type") String propertyType,
            @Query("property_name") String propertyName
    );
}
