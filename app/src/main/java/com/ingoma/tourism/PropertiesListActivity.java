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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ingoma.tourism.adapter.HotelAdapter;
import com.ingoma.tourism.model.HotelModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PropertiesListActivity extends AppCompatActivity {

    private LinearLayout Ll_sort;

    private RecyclerView hotelRecyclerView;
    private HotelAdapter hotelAdapter;
    private List<HotelModel> hotelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_properties_list);

        //padding status bar and bottom navigation bar
        View RootLayout = findViewById(R.id.detail_activity_lyt);
        View toolbar = findViewById(R.id.htab_appbar);
        paddingStatusBar(toolbar);
        paddingBottomNavigationBar(RootLayout);

        Ll_sort=(LinearLayout) findViewById(R.id.Ll_sort);
        Ll_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(PropertiesListActivity.this, PropertiesDetailsActivity.class));
            }
        });

        // Initialize RecyclerView
        hotelRecyclerView = findViewById(R.id.rvSrpList);
        hotelRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load hotels
        hotelList = getHotels();

        // Set adapter
        hotelAdapter = new HotelAdapter(this, hotelList);
        hotelRecyclerView.setAdapter(hotelAdapter);
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
}