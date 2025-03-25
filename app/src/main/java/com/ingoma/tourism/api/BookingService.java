package com.ingoma.tourism.api;

import com.ingoma.tourism.model.Booking;
import com.ingoma.tourism.model.BookingDetailsResponse;
import com.ingoma.tourism.model.BookingListResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BookingService {

    @POST("api/v1/saveBooking") // Replace with your Laravel endpoint
    Call<Booking> saveBooking(@Body Booking booking);

    @GET("api/v1/hotel-bookings/user/{userId}")
    Call<BookingListResponse> getUserHotelBookings(
            @Path("userId") Long userId,
            @Query("per_page") int perPage,
            @Query("page") int page
    );

    @GET("api/v1/gueshouse-bookings/user/{userId}")
    Call<BookingListResponse> getUserGuestHouseBookings(
            @Path("userId") Long userId,
            @Query("per_page") int perPage,
            @Query("page") int page
    );

    @GET("api/v1/booking/details/{id}")
    Call<BookingDetailsResponse> getBookingDetails(@Path("id") Long bookingId);
}
