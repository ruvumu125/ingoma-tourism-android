package com.ingoma.tourism;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.ingoma.tourism.adapter.HotelAdapter;
import com.ingoma.tourism.dialog.EditBookingInfoDialogFragment;
import com.ingoma.tourism.dialog.GuestSelectionDialogFragment;
import com.ingoma.tourism.dialog.PropertySortDialogFragment;
import com.ingoma.tourism.model.HotelModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PropertiesListActivity extends AppCompatActivity implements EditBookingInfoDialogFragment.CallBackListener {

    private LinearLayout Ll_sort;

    private RecyclerView hotelRecyclerView;
    private HotelAdapter hotelAdapter;
    private List<HotelModel> hotelList;

    private TextView toolbar_custom_title,tv_date_guest;
    private LinearLayout Ll_date_guest_infos;

    String checkinDate,checkoutDate,checkinDateFrench,checkoutDateFrench,city_or_property,nb_adultes,nb_enfants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_properties_list);

        //padding status bar and bottom navigation bar
        View RootLayout = findViewById(R.id.detail_activity_lyt);
        View toolbar = findViewById(R.id.htab_appbar);
        paddingStatusBar(toolbar);
        paddingBottomNavigationBar(RootLayout);

        //initialize views
        toolbar_custom_title=findViewById(R.id.toolbar_custom_title);
        tv_date_guest=findViewById(R.id.tv_date_guest);
        Ll_date_guest_infos=findViewById(R.id.Ll_date_guest_infos);
        Ll_sort=(LinearLayout) findViewById(R.id.Ll_sort);

        // Get default dates from hotel search activity
        Intent intent = getIntent();
        checkinDate = intent.getStringExtra("checkinDate");
        checkoutDate = intent.getStringExtra("checkoutDate");
        checkinDateFrench = intent.getStringExtra("checkinDateFrench");
        checkoutDateFrench = intent.getStringExtra("checkoutDateFrench");
        city_or_property = intent.getStringExtra("city_or_property");
        nb_adultes = intent.getStringExtra("nb_adultes");
        nb_enfants = intent.getStringExtra("nb_enfants");

        //set textview
        toolbar_custom_title.setText(city_or_property);
        String guest_info=displayGuestInfo(nb_adultes,nb_enfants);
        tv_date_guest.setText(checkinDateFrench+" - "+checkoutDateFrench+", "+guest_info);


        Ll_sort.setOnClickListener(view -> {

            PropertySortDialogFragment propertySortDialogFragment = new PropertySortDialogFragment();
            propertySortDialogFragment.show(getSupportFragmentManager(), "PropertySortBottomSheetDialog");
        });

        // Initialize RecyclerView
        hotelRecyclerView = findViewById(R.id.rvSrpList);
        hotelRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load hotels
        hotelList = getHotels();

        // Set adapter
        hotelAdapter = new HotelAdapter(this, hotelList);
        hotelRecyclerView.setAdapter(hotelAdapter);

        Ll_date_guest_infos.setOnClickListener(view -> {

            EditBookingInfoDialogFragment editBookingInfoDialogFragment = new EditBookingInfoDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString("city_or_property", city_or_property);
            bundle.putString("checkinDate", checkinDate);
            bundle.putString("checkoutDate", checkoutDate);
            bundle.putString("nb_adultes", nb_adultes);
            bundle.putString("nb_enfants", nb_enfants);
            editBookingInfoDialogFragment.setArguments(bundle);
            editBookingInfoDialogFragment.show(getSupportFragmentManager(), "EditBookingInfoBottomSheetDialog");
        });
    }

    private String displayGuestInfo(String nb_adultes,String nb_enfants){

        String guest_info="";
        if (Integer.valueOf(nb_adultes)>1){

            if (Integer.valueOf(nb_enfants)>0){

                if (Integer.valueOf(nb_enfants)==1){
                    guest_info=nb_adultes+" adultes"+" "+nb_enfants+" enfant";
                }
                else {
                    guest_info=nb_adultes+" adultes"+" "+nb_enfants+" enfants";
                }

            } else{
                guest_info=nb_adultes+" adultes";
            }
        }
        else{

            if (Integer.valueOf(nb_enfants)>0){

                if (Integer.valueOf(nb_enfants)==1){
                    guest_info=nb_adultes+" adulte"+" "+nb_enfants+" enfant";
                }
                else {
                    guest_info=nb_adultes+" adulte"+" "+nb_enfants+" enfants";
                }

            } else{
                guest_info=nb_adultes+" adulte";
            }
        }

        return guest_info;
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

    // Dummy hotel data
    private List<HotelModel> getHotels() {
        List<HotelModel> hotels = new ArrayList<>();

        hotels.add(new HotelModel(
                "Luxury Palace", "5-Star Hotel", "123 Main St, NY", 299.99, 4.8,
                Arrays.asList(
                        "https://cf.bstatic.com/xdata/images/hotel/max1024x768/612233979.jpg?k=16fc7dcbbd3f37e7f91d17cc04885f949b57c4d0bd1161f665591013c6e6429d&o=&hp=1",
                        "https://cf.bstatic.com/xdata/images/hotel/max1024x768/612232552.jpg?k=a039edf0528c2cea465c822003bc9e051933ffcf70a61d1b8072c7501c72e992&o=&hp=1",
                        "https://cf.bstatic.com/xdata/images/hotel/max1024x768/611043161.jpg?k=c65d51b5a686e9d39a2915801abb12d921cddeefa26a236d22d68dbda4c20c6c&o=&hp=1"
                ),
                Arrays.asList("Free WiFi", "Pool", "Gym", "Restaurant")
        ));

        hotels.add(new HotelModel(
                "Beachside Resort", "4-Star Hotel", "456 Ocean Ave, CA", 199.99, 4.5,
                Arrays.asList(
                        "https://cf.bstatic.com/xdata/images/hotel/max1024x768/562876208.jpg?k=a40bddfa46105764b740352aa1e7be5b9a2d7a058c1c173ca0bc2111785c59d9&o=&hp=1",
                        "https://cf.bstatic.com/xdata/images/hotel/max1024x768/562876934.jpg?k=1975075eb6b1fbdbd5b64a2db201f9f01a0353b7f7836f17d9bfc229802fb1ed&o=&hp=1",
                        "https://cf.bstatic.com/xdata/images/hotel/max1024x768/562878044.jpg?k=9b01963b477d5fab867e505a6b5b1de55d81b2fa064e2ba96a87bcd024f3c06d&o=&hp=1"
                ),
                Arrays.asList("Beach Access", "Spa", "Bar", "Free Breakfast")
        ));

        hotels.add(new HotelModel(
                "Mountain Retreat", "Boutique Hotel", "789 Hilltop Rd, CO", 149.99, 4.2,
                Arrays.asList(
                        "https://cf.bstatic.com/xdata/images/hotel/max1024x768/28306283.jpg?k=5d154f1cfa47ee4da7c9f605093590248b4baa0464668ec197cbc3e453b89455&o=&hp=1",
                        "https://cf.bstatic.com/xdata/images/hotel/max1024x768/28306423.jpg?k=2e99cceba1ab79e656898467fd792f7b39ac19939731ed077099a478e61b5d63&o=&hp=1",
                        "https://cf.bstatic.com/xdata/images/hotel/max1024x768/28306353.jpg?k=0dab1d1a8acaa1b746135f5e44e21f72bd9089d53012980bc0d735ffc41066d2&o=&hp=1"
                ),
                Arrays.asList("Hiking Trails", "Hot Tub", "Pet Friendly", "Fireplace")
        ));

        return hotels;
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
        toolbar_custom_title.setText(city_or_property_response);
        String guest_info=displayGuestInfo(String.valueOf(adultesNumber_response),String.valueOf(childrenNumber_response));
        tv_date_guest.setText(checkinDateFrench+" - "+checkoutDateFrench+", "+guest_info);
    }
}