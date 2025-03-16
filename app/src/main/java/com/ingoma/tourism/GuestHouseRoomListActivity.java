package com.ingoma.tourism;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowInsets;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ingoma.tourism.adapter.GuestHouseVariantAdapter;
import com.ingoma.tourism.adapter.RoomGuestHouseAdapter;
import com.ingoma.tourism.api.PropertyApiService;
import com.ingoma.tourism.api.Retrofit2Client;
import com.ingoma.tourism.dialog.EditBookingInfoDialogFragment;
import com.ingoma.tourism.model.GuestHouse;
import com.ingoma.tourism.model.GuestHouseVariant;
import com.ingoma.tourism.model.Plan;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuestHouseRoomListActivity extends AppCompatActivity implements EditBookingInfoDialogFragment.CallBackListener {

    private Retrofit2Client retrofit2Client;
    private PropertyApiService propertyApiService;

    private String property_adress,property_first_image,property_id,property_name,property_type,checkinDate,checkoutDate,checkinDateFrench,checkoutDateFrench,city_or_property,nb_adultes,nb_enfants;
    private LinearLayout continue_btn;
    private TextView toolbarTitle,tv_booking_info,unStrikedPrice,txtPerNight,continue_btn_tv;
    private LinearLayout booking_info_edit_section;

    private ConstraintLayout section_footer_price;
    private View section_skeleton_footer_price;
    private LinearLayout section_content,section_skleton,section_error;
    private ConstraintLayout section_price;

    private RecyclerView roomTypesRecyclerView;
    private RecyclerView guestHouseVariantsRecyclerView;
    private GuestHouseVariant selectedGuestHouseVariant = null;
    private String property_price="";
    private String price_currency="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_house_room_list);

        retrofit2Client=new Retrofit2Client(getApplicationContext());
        propertyApiService = retrofit2Client.createService(PropertyApiService.class);

        //padding status bar and bottom navigation bar
        View RootLayout = findViewById(R.id.cordinatorLytRoom);
        View toolbar = findViewById(R.id.htab_appbar_room);
        paddingStatusBar(toolbar);
        paddingBottomNavigationBar(RootLayout);

        // initialisation
        //rvRooms = findViewById(R.id.rvSrpList);
        toolbarTitle = findViewById(R.id.toolbar_custom_title);
        tv_booking_info = findViewById(R.id.toolbar_custom_sub_title);
        booking_info_edit_section = findViewById(R.id.lytSubtitleWrap);

        unStrikedPrice=findViewById(R.id.unStrikedPrice);
        txtPerNight=findViewById(R.id.txtPerNight);
        continue_btn = findViewById(R.id.lyt_cta);
        continue_btn_tv = findViewById(R.id.select_room_btn_tv);

        section_content=findViewById(R.id.section_content_guest_house_room);
        section_skleton=findViewById(R.id.section_skleton_guest_house_room);
        section_price=findViewById(R.id.detail_price_offer_lyt);
        section_error=findViewById(R.id.section_error_guest_house_room);
        section_footer_price=findViewById(R.id.lytInStock);
        section_skeleton_footer_price=findViewById(R.id.hd_footer_shimmer);
        continue_btn_tv.setText("Continuer");

        roomTypesRecyclerView = findViewById(R.id.rvGuestHouseRoom);
        guestHouseVariantsRecyclerView = findViewById(R.id.rvGuestHouseTarification);
        //rvRooms.setLayoutManager(new LinearLayoutManager(this));

        // Get default dates from hotel list activity
        Intent intent = getIntent();
        if (intent != null) {

            property_id = intent.getStringExtra("property_id");
            property_name = intent.getStringExtra("property_name");
            property_adress = intent.getStringExtra("property_adress");
            property_first_image = intent.getStringExtra("property_first_image");
            checkinDate = intent.getStringExtra("checkinDate");
            checkoutDate = intent.getStringExtra("checkoutDate");
            checkinDateFrench = intent.getStringExtra("checkinDateFrench");
            checkoutDateFrench = intent.getStringExtra("checkoutDateFrench");
            city_or_property = intent.getStringExtra("city_or_property");
            nb_adultes = intent.getStringExtra("nb_adultes");
            nb_enfants = intent.getStringExtra("nb_enfants");
            property_type = intent.getStringExtra("property_type");

            toolbarTitle.setText(property_name);

            String guest_info=displayGuestInfo(property_type,nb_adultes,nb_enfants);
            tv_booking_info.setText(checkinDateFrench+" - "+checkoutDateFrench+guest_info);
        }

        //jjjj
        booking_info_edit_section.setOnClickListener(view -> {

            EditBookingInfoDialogFragment editBookingInfoDialogFragment = new EditBookingInfoDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString("city_or_property", city_or_property);
            bundle.putString("checkinDate", checkinDate);
            bundle.putString("checkoutDate", checkoutDate);
            bundle.putString("nb_adultes", nb_adultes);
            bundle.putString("nb_enfants", nb_enfants);
            bundle.putString("property_type", property_type);
            bundle.putString("provenance", "property_room_listing_activity");
            editBookingInfoDialogFragment.setArguments(bundle);
            editBookingInfoDialogFragment.show(getSupportFragmentManager(), "EditBookingInfoBottomSheetDialog");

        });

        continue_btn.setOnClickListener(view -> {

            if (isAnyGuestHouseVariantSelected()) {
                openConfirmHotelBookingActivity();
            } else {
                Toast.makeText(this, "No Plan Selected!", Toast.LENGTH_SHORT).show();
            }
        });

        roomTypesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        guestHouseVariantsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        fetchGuestHouseRoom(Long.parseLong(property_id));
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

    private String displayGuestInfo(String property_type,String nb_adultes,String nb_enfants){

        String guest_info="";

        if (property_type.equals("hotel")){

            if (Integer.valueOf(nb_adultes)>1){

                if (Integer.valueOf(nb_enfants)>0){

                    if (Integer.valueOf(nb_enfants)==1){
                        guest_info=" ,"+nb_adultes+" adultes"+" "+nb_enfants+" enfant";
                    }
                    else {
                        guest_info=" ,"+nb_adultes+" adultes"+" "+nb_enfants+" enfants";
                    }

                } else{
                    guest_info=" ,"+nb_adultes+" adultes";
                }
            }
            else{

                if (Integer.valueOf(nb_enfants)>0){

                    if (Integer.valueOf(nb_enfants)==1){
                        guest_info=" ,"+nb_adultes+" adulte"+" "+nb_enfants+" enfant";
                    }
                    else {
                        guest_info=" ,"+nb_adultes+" adulte"+" "+nb_enfants+" enfants";
                    }

                } else{
                    guest_info=" ,"+nb_adultes+" adulte";
                }
            }

        }

        return guest_info;
    }

    private void fetchGuestHouseRoom(Long property_id) {

        section_content.setVisibility(View.GONE);
        section_skleton.setVisibility(View.VISIBLE);
        section_error.setVisibility(View.GONE);
        section_skeleton_footer_price.setVisibility(View.VISIBLE);
        section_footer_price.setVisibility(View.GONE);
        section_price.setVisibility(View.GONE);

        Call<GuestHouse> call = propertyApiService.getGuestHouseRooms(property_id);

        call.enqueue(new Callback<GuestHouse>() {
            @Override
            public void onResponse(Call<GuestHouse> call, Response<GuestHouse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GuestHouse guestHouse = response.body();

                    // Set up the RoomTypes RecyclerView
                    RoomGuestHouseAdapter roomGuestHouseAdapter = new RoomGuestHouseAdapter(getApplicationContext(),guestHouse.getRoomtypes());
                    roomTypesRecyclerView.setAdapter(roomGuestHouseAdapter);

                    // Set up the GuestHouseVariants RecyclerView
                    GuestHouseVariantAdapter guestHouseVariantAdapter = new GuestHouseVariantAdapter(guestHouse.getGuest_house_variants(),guestHouseVariant -> {

                        selectedGuestHouseVariant = guestHouseVariant;
                        unStrikedPrice.setText(String.valueOf(guestHouseVariant.getPrice()));
                        txtPerNight.setText(guestHouseVariant.getCurrency());

                        property_price=String.valueOf(guestHouseVariant.getPrice());
                        price_currency=guestHouseVariant.getCurrency();

                        section_price.setVisibility(View.VISIBLE);
                    });
                    guestHouseVariantsRecyclerView.setAdapter(guestHouseVariantAdapter);

                    section_content.setVisibility(View.VISIBLE);
                    section_skleton.setVisibility(View.GONE);
                    section_error.setVisibility(View.GONE);
                    section_skeleton_footer_price.setVisibility(View.GONE);
                   section_footer_price.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<GuestHouse> call, Throwable t) {

                Toast.makeText(GuestHouseRoomListActivity.this, t.getMessage().toString(), Toast.LENGTH_LONG).show();
                section_content.setVisibility(View.GONE);
                section_skleton.setVisibility(View.GONE);
                section_error.setVisibility(View.VISIBLE);

                section_skeleton_footer_price.setVisibility(View.GONE);
                section_footer_price.setVisibility(View.GONE);

            }
        });
    }

    private void openConfirmHotelBookingActivity() {
        Intent intent = new Intent(this, ConfirmGuestHouseBookingActivity.class);

        intent.putExtra("property_id", property_id);
        intent.putExtra("property_type", property_type);
        intent.putExtra("property_name", property_name);
        intent.putExtra("property_adress", property_adress);
        intent.putExtra("property_first_image", property_first_image);
        intent.putExtra("property_type", property_type);
        intent.putExtra("checkinDate", checkinDate);
        intent.putExtra("checkoutDate", checkoutDate);
        intent.putExtra("checkinDateFrench", checkinDateFrench);
        intent.putExtra("checkoutDateFrench", checkoutDateFrench);
        intent.putExtra("city_or_property", city_or_property);
        intent.putExtra("nb_adultes", nb_adultes);
        intent.putExtra("nb_enfants",nb_enfants);

        intent.putExtra("property_price",property_price);
        intent.putExtra("price_currency",price_currency);



        startActivity(intent);
    }

    // Method to check if any plan is selected
    private boolean isAnyGuestHouseVariantSelected() {
        return selectedGuestHouseVariant != null;
    }

    @Override
    public void onModifyButtonClicked(String city_or_property_response, String checkinDate_response, String checkoutDate_response, String checkinDateFrenchFormat_response, String checkoutDateFrenchFormat_response, int adultesNumber_response, int childrenNumber_response) {

        checkinDate=checkinDate_response;
        checkoutDate=checkoutDate_response;
        checkinDateFrench=checkinDateFrenchFormat_response;
        checkoutDateFrench=checkoutDateFrenchFormat_response;
        city_or_property=city_or_property_response;
        nb_adultes=String.valueOf(adultesNumber_response);
        nb_enfants=String.valueOf(childrenNumber_response);

        //set textview
        String guest_info=displayGuestInfo(property_type,String.valueOf(adultesNumber_response),String.valueOf(childrenNumber_response));
        tv_booking_info.setText(checkinDateFrench+" - "+checkoutDateFrench+guest_info);
    }

    @Override
    public void onDialogFragmentDismiss() {

    }
}