package com.ingoma.tourism;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowInsets;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.ingoma.tourism.adapter.BookingDetailsAmenitiesAdapter;
import com.ingoma.tourism.adapter.LandmarksAdapter;
import com.ingoma.tourism.adapter.PropertyAmenitiesAdapter;
import com.ingoma.tourism.adapter.RulesAdapter;
import com.ingoma.tourism.adapter.SimilarPropertiesAdapter;
import com.ingoma.tourism.adapter.SliderPropertyDetailsAdapter;
import com.ingoma.tourism.api.BookingService;
import com.ingoma.tourism.api.PropertyApiService;
import com.ingoma.tourism.api.Retrofit2Client;
import com.ingoma.tourism.constant.Constant;
import com.ingoma.tourism.model.BookingDetails;
import com.ingoma.tourism.model.BookingDetailsGuestDetails;
import com.ingoma.tourism.model.BookingDetailsProperty;
import com.ingoma.tourism.model.BookingDetailsResponse;
import com.ingoma.tourism.model.BookingDetailsRoom;
import com.ingoma.tourism.model.BookingDetailsRoomAmenity;
import com.ingoma.tourism.model.Landmark;
import com.ingoma.tourism.model.PropertyAmenity;
import com.ingoma.tourism.model.PropertyDetails;
import com.ingoma.tourism.model.PropertyDetailsResponse;
import com.ingoma.tourism.model.Rule;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingDetailsActivity extends AppCompatActivity {

    private TextView tv_booking_details_num_reservation,
            tv_booking_details_date_reservation,tv_booking_details_date_arrivee,
            tv_booking_details_date_depart,tv_booking_details_duree_sejour,
            tv_booking_details_prix_unitaire,tv_booking_details_total_reservation,
            tv_booking_details_nombre_adultes,tv_booking_details_nombre_enfants,tv_booking_details_statut;
    private AppCompatTextView tv_booking_details_hotel_name,tv_booking_details_hotel_address,tv_booking_details_hotel_address_road;
    private ShapeableImageView iv_booked_hotel_img;
    private AppCompatImageView iv_booking_details_room_imageView;
    private AppCompatTextView tv_booking_details_room_name,tv_booking_details_guest_count,tv_booking_details_bed_type,tv_booking_details_room_surface;
    private RecyclerView rv_room_amenities;
    private TextView tv_booking_details_guest_firstname,tv_booking_details_guest_lastname,tv_booking_details_guest_phone,tv_booking_details_guest_email;

    private LinearLayout sec_content,sec_skeleton,sec_error;
    private Retrofit2Client retrofit2Client;
    private BookingService bookingService;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        Long bookingId = getIntent().getLongExtra("idBooking", -1); // Default value to avoid null


        retrofit2Client=new Retrofit2Client(getApplicationContext());
        bookingService = retrofit2Client.createService(BookingService.class);

        View RootLayout = findViewById(R.id.cordinatorLyt);
        View toolbar = findViewById(R.id.htab_appbar);
        paddingStatusBar(toolbar);
        paddingBottomNavigationBar(RootLayout);

        //initialisation
        tv_booking_details_num_reservation=findViewById(R.id.tv_booking_details_num_reservation);
        tv_booking_details_date_reservation=findViewById(R.id.tv_booking_details_date_reservation);
        tv_booking_details_date_arrivee=findViewById(R.id.tv_booking_details_date_arrivee);
        tv_booking_details_date_depart=findViewById(R.id.tv_booking_details_date_depart);
        tv_booking_details_duree_sejour=findViewById(R.id.tv_booking_details_duree_sejour);
        tv_booking_details_prix_unitaire=findViewById(R.id.tv_booking_details_prix_unitaire);
        tv_booking_details_total_reservation=findViewById(R.id.tv_booking_details_total_reservation);
        tv_booking_details_nombre_adultes=findViewById(R.id.tv_booking_details_nombre_adultes);
        tv_booking_details_nombre_enfants=findViewById(R.id.tv_booking_details_nombre_enfants);
        tv_booking_details_statut=findViewById(R.id.tv_booking_details_statut);

        tv_booking_details_hotel_name=findViewById(R.id.tv_booking_details_hotel_name);
        tv_booking_details_hotel_address=findViewById(R.id.tv_booking_details_hotel_address);
        iv_booked_hotel_img=findViewById(R.id.iv_booked_hotel_img);

        iv_booking_details_room_imageView=findViewById(R.id.iv_booking_details_room_imageView);
        tv_booking_details_room_name=findViewById(R.id.tv_booking_details_room_name);
        tv_booking_details_guest_count=findViewById(R.id.tv_booking_details_guest_count);
        tv_booking_details_bed_type=findViewById(R.id.tv_booking_details_bed_type);
        tv_booking_details_room_surface=findViewById(R.id.tv_booking_details_room_surface);
        rv_room_amenities=findViewById(R.id.rv_room_amenities);

        tv_booking_details_guest_firstname=findViewById(R.id.tv_booking_details_guest_firstname);
        tv_booking_details_guest_lastname=findViewById(R.id.tv_booking_details_guest_lastname);
        tv_booking_details_guest_phone=findViewById(R.id.tv_booking_details_guest_phone);
        tv_booking_details_guest_email=findViewById(R.id.tv_booking_details_guest_email);

        sec_content=findViewById(R.id.sec_content);
        sec_skeleton=findViewById(R.id.sec_skeleton);
        sec_error=findViewById(R.id.sec_error);

        fetchProperty(bookingId);
    }

    public int getStatusBarHeight() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = 0;
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }

        // Convert 10dp to pixels
        int extraHeight = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                10,
                Resources.getSystem().getDisplayMetrics()
        );

        return statusBarHeight + extraHeight;
    }

    private void paddingStatusBar(View layout){
        int statusBarHeight = getStatusBarHeight();
        layout.setPadding(layout.getPaddingLeft(), statusBarHeight, layout.getPaddingRight(), layout.getPaddingBottom());

    }

    public int getNavigationBarHeight(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { // Android 11+
            WindowInsets insets = activity.getWindowManager().getCurrentWindowMetrics().getWindowInsets();
            return insets.getInsets(WindowInsets.Type.navigationBars()).bottom;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // Android 6+
            View decorView = activity.getWindow().getDecorView();
            return decorView.getRootWindowInsets().getStableInsetBottom();
        } else { // Older versions (pre-Marshmallow)
            Resources resources = activity.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            return resourceId > 0 ? resources.getDimensionPixelSize(resourceId) : 0;
        }
    }

    private void paddingBottomNavigationBar(View layout){

        // Get the system bottom inset (dynamic)
        int navBarHeight = getNavigationBarHeight(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            layout.setOnApplyWindowInsetsListener((v, insets) -> {

                v.setPadding(0, 0, 0, navBarHeight);
                return insets;
            });
        } else {

            layout.setPadding(0, 0, 0, navBarHeight);
        }
    }

    private void fetchProperty(Long id) {

        sec_content.setVisibility(View.GONE);
        sec_skeleton.setVisibility(View.VISIBLE);
        sec_error.setVisibility(View.GONE);

        bookingService.getBookingDetails(id).enqueue(new Callback<BookingDetailsResponse>() {
            @Override
            public void onResponse(Call<BookingDetailsResponse> call, Response<BookingDetailsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    BookingDetails bookingDetails = response.body().getBooking();

                    //booking details
                    tv_booking_details_num_reservation.setText(bookingDetails.getBookingNumber());
                    tv_booking_details_date_reservation.setText(bookingDetails.getBookingDate());
                    tv_booking_details_date_arrivee.setText(bookingDetails.getCheckInDate());
                    tv_booking_details_date_depart.setText(bookingDetails.getCheckOutDate());
                    tv_booking_details_duree_sejour.setText(bookingDetails.getDuration());
                    tv_booking_details_prix_unitaire.setText(bookingDetails.getUnitPrice());
                    tv_booking_details_total_reservation.setText(bookingDetails.getTotalPrice());
                    tv_booking_details_nombre_adultes.setText(String.valueOf(bookingDetails.getAdults()));
                    tv_booking_details_nombre_enfants.setText(String.valueOf(bookingDetails.getChildren()));
                    tv_booking_details_statut.setText(bookingDetails.getStatus());

                    //property details
                    tv_booking_details_hotel_name.setText(bookingDetails.getProperty().getName());
                    tv_booking_details_hotel_address.setText(bookingDetails.getProperty().getAddress());
                    String baseUrlProperty = Constant.BASE_URL + "api/v1/property-image/";
                    String fullImageUrlProperty = baseUrlProperty+ bookingDetails.getProperty().getImageUrl();
                    Glide.with(getApplicationContext())
                            .load(fullImageUrlProperty)
                            .placeholder(R.drawable.hotel_place_holder)
                            .into(iv_booked_hotel_img);

                    if (bookingDetails.getBookingType().equals("hotel")){

                        List<BookingDetailsRoomAmenity> room_amenities= bookingDetails.getRoom().getAmenities();

                        //room details
                        String baseUrlRoom = Constant.BASE_URL + "api/v1/room-image/";
                        String fullImageUrlRoom = baseUrlRoom+ bookingDetails.getRoom().getImageUrl();
                        Glide.with(getApplicationContext())
                                .load(fullImageUrlRoom)
                                .placeholder(R.drawable.hotel_place_holder)
                                .into(iv_booking_details_room_imageView);
                        tv_booking_details_room_name.setText(bookingDetails.getRoom().getTypeName());
                        tv_booking_details_guest_count.setText(String.valueOf(bookingDetails.getRoom().getMaxGuests()));
                        tv_booking_details_bed_type.setText(bookingDetails.getRoom().getBedType());
                        tv_booking_details_room_surface.setText(bookingDetails.getRoom().getRoomSize());

                        //room amenities
                        rv_room_amenities.setAdapter(new BookingDetailsAmenitiesAdapter(room_amenities));

                    }


                    //gest infos
                    tv_booking_details_guest_firstname.setText(bookingDetails.getGuestDetail().getFirstName());
                    tv_booking_details_guest_lastname.setText(bookingDetails.getGuestDetail().getLastName());
                    tv_booking_details_guest_phone.setText(bookingDetails.getGuestDetail().getPhone());
                    tv_booking_details_guest_email.setText(bookingDetails.getGuestDetail().getEmail());

                    sec_content.setVisibility(View.VISIBLE);
                    sec_skeleton.setVisibility(View.GONE);
                    sec_error.setVisibility(View.GONE);


                } else {

                    sec_content.setVisibility(View.GONE);
                    sec_skeleton.setVisibility(View.GONE);
                    sec_error.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<BookingDetailsResponse> call, Throwable t) {
                sec_content.setVisibility(View.GONE);
                sec_skeleton.setVisibility(View.GONE);
                sec_error.setVisibility(View.VISIBLE);
            }
        });
    }

}