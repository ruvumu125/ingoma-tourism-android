package com.ingoma.tourism;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.ingoma.tourism.adapter.AmenitiesAdapter;
import com.ingoma.tourism.adapter.LandmarksAdapter;
import com.ingoma.tourism.adapter.RulesAdapter;
import com.ingoma.tourism.adapter.SimilarHotelsAdapter;
import com.ingoma.tourism.adapter.SliderPropertyDetailsAdapter;
import com.ingoma.tourism.model.Amenity;
import com.ingoma.tourism.model.Hotel;
import com.ingoma.tourism.model.Landmark;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PropertiesDetailsActivity extends AppCompatActivity {

    private NestedScrollView scrollView;
    private ConstraintLayout section_sliders;
    private LinearLayout hotel_amenities_section,hotel_rules_section;
    private FrameLayout fr_favorite;

    private SliderView sliderView;
    private RecyclerView rvAmenities, rvRules, rvLandmarks, rvSimilarHotels;
    private WebView webViewDescription;
    private AppCompatTextView tvHotelType, tvRating;
    private TextView tvHotelName,tvAddress,tvPrice;

    private List<String> imageUrls = new ArrayList<>();
    private List<Amenity> amenities = new ArrayList<>();
    private List<String> rules = new ArrayList<>();
    private List<Landmark> landmarks = new ArrayList<>();
    private List<Hotel> similarHotelsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_properties_details);

        //padding status bar and bottom navigation bar
        View RootLayout = findViewById(R.id.rootLayout);
        View hiddenToolbar = findViewById(R.id.title_bar);
        paddingStatusBar(hiddenToolbar);
        paddingBottomNavigationBar(RootLayout);


        // Initialize Views
        sliderView = findViewById(R.id.photos_rv);
        rvAmenities = findViewById(R.id.rvAmenities);
        rvRules = findViewById(R.id.recyclerPolicies);
        rvLandmarks = findViewById(R.id.recyclerLandmarks);
        rvSimilarHotels = findViewById(R.id.recyclerSimilarProperties);
        webViewDescription = findViewById(R.id.mlv_goods_detail_description);
        tvHotelName = findViewById(R.id.prop_name);
        tvHotelType = findViewById(R.id.tv_hotel_type);
        tvAddress = findViewById(R.id.tv_address);
        tvPrice = findViewById(R.id.unStrikedPrice);
        tvRating = findViewById(R.id.rating_bar);


        // Load Data
        loadHotelData();

        // Set up Image Slider
        SliderPropertyDetailsAdapter sliderAdapter = new SliderPropertyDetailsAdapter(imageUrls);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.startAutoCycle();

        // Set up WebView
        webViewDescription.loadDataWithBaseURL(null, "<html><body style='text-align: justify;'>Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting</body></html>", "text/html", "UTF-8", null);

        // Set up RecyclerViews
        rvAmenities.setLayoutManager(new LinearLayoutManager(this));
        rvAmenities.setAdapter(new AmenitiesAdapter(amenities));

        rvRules.setLayoutManager(new LinearLayoutManager(this));
        rvRules.setAdapter(new RulesAdapter(rules));

        rvLandmarks.setLayoutManager(new LinearLayoutManager(this));
        rvLandmarks.setAdapter(new LandmarksAdapter(landmarks));

        rvSimilarHotels.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvSimilarHotels.setAdapter(new SimilarHotelsAdapter(getApplicationContext(),similarHotelsList));



        //tab
        hotel_rules_section=(LinearLayout) findViewById(R.id.hotel_rules_section);
        hotel_amenities_section=(LinearLayout) findViewById(R.id.hotel_amenities_section);
        section_sliders=(ConstraintLayout) findViewById(R.id.section_sliders);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        fr_favorite=(FrameLayout)findViewById(R.id.fr_favorite);
        fr_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(PropertiesDetailsActivity.this, PropertyRoomListActivity.class));
            }
        });

        // Handle Tab Clicks (Scroll to Sections)
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    // Scroll to About Us Section
                    scrollView.post(() -> scrollView.scrollTo(0, section_sliders.getTop()));
                } else if (tab.getPosition() == 1) {
                    // Scroll to Mission Section
                    scrollView.post(() -> scrollView.scrollTo(0, hotel_amenities_section.getTop()));
                } else if (tab.getPosition() == 2) {
                    // Scroll to Mission Section
                    scrollView.post(() -> scrollView.scrollTo(0, hotel_rules_section.getTop()));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });



        // Enable edge-to-edge mode (content behind system bars)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        scrollView=(NestedScrollView) findViewById(R.id.customScrollViewID);

//        scrollView=(NestedScrollView) findViewById(R.id.customScrollViewID);
//        SliderView sliderView = findViewById(R.id.photos_rv);
//
//        List<String> imageUrls = Arrays.asList(
//                "https://pix8.agoda.net/hotelImages/21673708/459353622/f05137b9c8d362688d50bb648708b394.jpeg?ce=0&s=1024x",
//                "https://q-xx.bstatic.com/xdata/images/hotel/max1024x768/566847549.jpg?k=8e58cee79f87274c6f69fe019bc228395dad2d5ad82528bf88031d0730937c3c&o=&s=1024x",
//                "https://q-xx.bstatic.com/xdata/images/hotel/max1024x768/438984381.jpg?k=1b58c40713632c9883aa862caba5e6a0100bc83ef4aceed402712d3a640d44c2&o=&s=1024x"
//        );
//
//        SliderPropertyDetailsAdapter adapter = new SliderPropertyDetailsAdapter(imageUrls);
//        sliderView.setSliderAdapter(adapter);
//        sliderView.setAutoCycle(true);  // Enable auto-slide
//        sliderView.startAutoCycle();

        //hhhhhhh
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY > oldScrollY) {
                    hiddenToolbar.setVisibility(View.VISIBLE);

                    //tab
                    int sliderTop = section_sliders.getTop() - 100;
                    int amenitiesTop = hotel_amenities_section.getTop() - 100;
                    int rulesTop = hotel_rules_section.getTop() - 100;

                    if (scrollY >= sliderTop && scrollY < amenitiesTop && scrollY < rulesTop) {
                        tabLayout.getTabAt(0).select();
                    } else if (scrollY >= amenitiesTop) {
                        tabLayout.getTabAt(1).select();
                    }
                    else if (scrollY >= rulesTop) {
                        tabLayout.getTabAt(2).select();
                    }

                } else if (scrollY == 0) {
                    hiddenToolbar.setVisibility(View.GONE);
                }
            }
        });
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
        int leftPaddingInPx = (int) (12 * getResources().getDisplayMetrics().density);
        layout.setPadding(leftPaddingInPx, statusBarHeight, layout.getPaddingRight(), layout.getPaddingBottom());

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

    private void loadHotelData() {
        tvHotelName.setText("Luxury Grand Hotel");
        tvHotelType.setText("5-Star Hotel");
        tvAddress.setText("123 Street, City, Country");
        tvPrice.setText("$200 per night");
        tvRating.setText("4.8/5");



        // Images
        imageUrls.add("https://pix8.agoda.net/hotelImages/21673708/459353622/f05137b9c8d362688d50bb648708b394.jpeg?ce=0&s=1024x");
        imageUrls.add("https://q-xx.bstatic.com/xdata/images/hotel/max1024x768/566847549.jpg?k=8e58cee79f87274c6f69fe019bc228395dad2d5ad82528bf88031d0730937c3c&o=&s=1024x");
        imageUrls.add("https://q-xx.bstatic.com/xdata/images/hotel/max1024x768/438984381.jpg?k=1b58c40713632c9883aa862caba5e6a0100bc83ef4aceed402712d3a640d44c2&o=&s=1024x");

        // Amenities
        amenities.add(new Amenity("Free WiFi",""));
        amenities.add(new Amenity("Swimming Pool",""));
        amenities.add(new Amenity("Parking",""));
        amenities.add(new Amenity("Breakfast",""));

        // Rules
        rules.add("Check-in: 2 PM");
        rules.add("Check-out: 11 AM");
        rules.add("No smoking allowed");

        // Landmarks
        landmarks.add(new Landmark("Palais des arts" , "2 km"));
        landmarks.add(new Landmark("Kigali Convetion Center", "5 km"));

        //similar property
        similarHotelsList.add(new Hotel("Grand Plaza", "Luxury", "New York, USA", "https://cf.bstatic.com/xdata/images/hotel/max1024x768/508071175.jpg?k=cfe923d6b90fbca85384bb067846afcd5363fd37b61d3614b5945f58a73d083b&o=&hp=1", 4.5, 120));
        similarHotelsList.add(new Hotel("Ocean View", "Resort", "Miami, USA", "https://cf.bstatic.com/xdata/images/hotel/max1024x768/508079694.jpg?k=1a1f5a174fedbc35ac2406fc3464d4c0cc5db0ba00570a65d522f79a4c37bef0&o=&hp=1", 4.3, 150));
        similarHotelsList.add(new Hotel("Skyline Inn", "Business", "Chicago, USA", "https://cf.bstatic.com/xdata/images/hotel/max1024x768/508071222.jpg?k=2b64145ff11293391cffe3cd3a40386e0abbe99e731d11e8127c0aeb691ea214&o=&hp=1", 4.7, 100));
    }




}