package com.ingoma.tourism.api;

import com.ingoma.tourism.model.LocationSearch;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LocationSearchService {

    @GET("api/v1/destination-search") // Assuming the endpoint is /api/search
    Call<List<LocationSearch>> searchLocation(@Query("query") String query,
                                              @Query("property_type") String propertyType);
}
