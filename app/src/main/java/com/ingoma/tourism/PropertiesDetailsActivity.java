package com.ingoma.tourism;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.ingoma.tourism.adapter.PropertyAmenitiesAdapter;
import com.ingoma.tourism.adapter.LandmarksAdapter;
import com.ingoma.tourism.adapter.RulesAdapter;
import com.ingoma.tourism.adapter.SimilarPropertiesAdapter;
import com.ingoma.tourism.adapter.SliderPropertyDetailsAdapter;
import com.ingoma.tourism.api.PropertyApiService;
import com.ingoma.tourism.api.Retrofit2Client;
import com.ingoma.tourism.dialog.EditBookingInfoDialogFragment;
import com.ingoma.tourism.dialog.PropertyAmenitiesDialogFragment;
import com.ingoma.tourism.dialog.PropertyLandmarksDialogFragment;
import com.ingoma.tourism.dialog.PropertyRulesDialogFragment;
import com.ingoma.tourism.model.PropertyAmenity;
import com.ingoma.tourism.model.Landmark;
import com.ingoma.tourism.model.PropertyDetails;
import com.ingoma.tourism.model.PropertyDetailsResponse;
import com.ingoma.tourism.model.Rule;
import com.smarteist.autoimageslider.SliderView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropertiesDetailsActivity extends AppCompatActivity implements EditBookingInfoDialogFragment.CallBackListener{

    private NestedScrollView scrollView;
    private ConstraintLayout section_sliders;
    private LinearLayout hotel_amenities_section,hotel_rules_section;
    private FrameLayout fr_favorite;

    private SliderView sliderView;
    private RecyclerView rvAmenities, rvRules, rvLandmarks, rvSimilarProperties;
    private TextView tvDescription;
    private AppCompatTextView tvHotelType, tvRating;
    private TextView toolbarTitle,tvHotelName,tvAddress,tvPrice,tvBookingInfoDate,tvBookingInfoGuest,tvBookingInfoUpdate;

    private String property_adress,property_first_image,property_minimum_price,price_currency,property_type,checkinDate,checkoutDate,checkinDateFrench,checkoutDateFrench,city_or_property,nb_adultes,nb_enfants;
    private TextView view_all_amenities,view_all_rules,view_all_landmarks;
    private LinearLayout select_room_btn;
    private String property_id, property_name;
    private TabLayout tabLayout;

    private RelativeLayout section_content;
    private LinearLayout section_skleton,section_error;
    private ConstraintLayout section_footer_price;
    private View section_skeleton_footer_price;
    private TextView tv_property_price,tv_price_currency;

    private Retrofit2Client retrofit2Client;
    private PropertyApiService propertyApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_properties_details);

        retrofit2Client=new Retrofit2Client(getApplicationContext());
        propertyApiService = retrofit2Client.createService(PropertyApiService.class);

        //padding status bar and bottom navigation bar
        View RootLayout = findViewById(R.id.rootLayout);
        View hiddenToolbar = findViewById(R.id.title_bar);
        paddingStatusBar(hiddenToolbar);
        paddingBottomNavigationBar(RootLayout);


        // Initialize Views
        toolbarTitle = findViewById(R.id.toolbarTitle);
        tvBookingInfoDate = findViewById(R.id.tvBookingInfoDate);
        tvBookingInfoGuest = findViewById(R.id.tvBookingInfoGuest);
        tvBookingInfoUpdate = findViewById(R.id.tvBookingInfoUpdate);
        sliderView = findViewById(R.id.photos_rv);
        rvAmenities = findViewById(R.id.rvAmenities);
        rvRules = findViewById(R.id.recyclerPolicies);
        rvLandmarks = findViewById(R.id.recyclerLandmarks);
        rvSimilarProperties = findViewById(R.id.recyclerSimilarProperties);
        tvDescription = findViewById(R.id.mlv_goods_detail_description);
        tvHotelName = findViewById(R.id.prop_name);
        tvHotelType = findViewById(R.id.tv_hotel_type);
        tvAddress = findViewById(R.id.tv_address);
        tvPrice = findViewById(R.id.unStrikedPrice);
        tvRating = findViewById(R.id.rating_bar);
        view_all_amenities = findViewById(R.id.view_all_amenities);
        view_all_rules = findViewById(R.id.view_all_rules);
        view_all_landmarks = findViewById(R.id.view_all_landmarks);
        select_room_btn = findViewById(R.id.lyt_cta);
        scrollView=findViewById(R.id.customScrollViewID);
        section_content=findViewById(R.id.section_content);
        section_skleton=findViewById(R.id.section_skleton);
        section_footer_price=findViewById(R.id.lytInStock);
        section_skeleton_footer_price=findViewById(R.id.hd_footer_shimmer);
        tv_property_price=findViewById(R.id.unStrikedPrice);
        tv_price_currency=findViewById(R.id.txtPerNight);
        section_error=findViewById(R.id.section_error);

        hotel_rules_section=(LinearLayout) findViewById(R.id.hotel_rules_section);
        hotel_amenities_section=(LinearLayout) findViewById(R.id.hotel_amenities_section);
        section_sliders=(ConstraintLayout) findViewById(R.id.section_sliders);
        tabLayout = findViewById(R.id.tabLayout);


        // Get default dates from hotel list activity
        Intent intent = getIntent();
        if (intent != null) {

            property_id  = intent.getStringExtra("property_id");
            property_name = intent.getStringExtra("property_name");
            checkinDate = intent.getStringExtra("checkinDate");
            checkoutDate = intent.getStringExtra("checkoutDate");
            checkinDateFrench = intent.getStringExtra("checkinDateFrench");
            checkoutDateFrench = intent.getStringExtra("checkoutDateFrench");
            city_or_property = intent.getStringExtra("city_or_property");
            nb_adultes = intent.getStringExtra("nb_adultes");
            nb_enfants = intent.getStringExtra("nb_enfants");
            property_type = intent.getStringExtra("property_type");
            property_minimum_price = intent.getStringExtra("property_minimum_price");
            price_currency = intent.getStringExtra("price_currency");
            property_adress = intent.getStringExtra("property_adress");
            property_first_image = intent.getStringExtra("property_first_image");

            toolbarTitle.setText(property_name);

            displayBookingInfo(checkinDateFrench,checkoutDateFrench,nb_adultes,nb_enfants,tvBookingInfoDate,tvBookingInfoGuest);
        }

        // Setup RecyclerViews
        rvAmenities.setLayoutManager(new LinearLayoutManager(this));
        rvRules.setLayoutManager(new LinearLayoutManager(this));
        rvLandmarks.setLayoutManager(new LinearLayoutManager(this));
        rvSimilarProperties.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

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

        tvBookingInfoUpdate.setOnClickListener(view -> {

            EditBookingInfoDialogFragment editBookingInfoDialogFragment = new EditBookingInfoDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString("city_or_property", city_or_property);
            bundle.putString("checkinDate", checkinDate);
            bundle.putString("checkoutDate", checkoutDate);
            bundle.putString("nb_adultes", nb_adultes);
            bundle.putString("nb_enfants", nb_enfants);
            bundle.putString("property_type", property_type);
            bundle.putString("provenance", "property_details_activity");
            editBookingInfoDialogFragment.setArguments(bundle);
            editBookingInfoDialogFragment.show(getSupportFragmentManager(), "EditBookingInfoBottomSheetDialog");
        });

        select_room_btn.setOnClickListener(view -> {
            openPropertyRoomListActivity(property_id,property_name);
        });

        fetchProperty(Long.parseLong(property_id),tv_property_price,tv_price_currency,view_all_amenities,view_all_rules,view_all_landmarks);
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

    private void fetchProperty(Long id,TextView tv_property_minimum_price,TextView tv_currency,TextView view_all_amenities,TextView view_all_rules,TextView view_all_landmarks) {

        section_content.setVisibility(View.GONE);
        section_skleton.setVisibility(View.VISIBLE);
        section_skeleton_footer_price.setVisibility(View.VISIBLE);
        section_footer_price.setVisibility(View.GONE);
        section_error.setVisibility(View.GONE);

        propertyApiService.getProperty(id).enqueue(new Callback<PropertyDetailsResponse>() {
            @Override
            public void onResponse(Call<PropertyDetailsResponse> call, Response<PropertyDetailsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PropertyDetails property = response.body().getData();

                    tvHotelName.setText(property.getName());
                    if (property.getPropertyType().equals("hotel")){
                        tvHotelType.setText(property.getPropertyType());
                    }
                    else{
                        tvHotelType.setText("Maison de passage");
                    }

                    tvAddress.setText(property.getAddress());
                    tvDescription.setText(property.getDescription());


                    // Load PropertyImage Slider
                    sliderView.setSliderAdapter(new SliderPropertyDetailsAdapter(PropertiesDetailsActivity.this, property.getImages()));
                    sliderView.startAutoCycle();

                    // amenities
                    List<PropertyAmenity> displayedAmenities = property.getAmenities().size() > 5 ? property.getAmenities().subList(0, 5) : property.getAmenities();
                    rvAmenities.setAdapter(new PropertyAmenitiesAdapter(displayedAmenities));
                    if (property.getAmenities().size()>5){
                        view_all_amenities.setVisibility(View.VISIBLE);
                    }
                    else{
                        view_all_amenities.setVisibility(View.GONE);
                    }
                    view_all_amenities.setOnClickListener(view -> {
                        showAllPropertyAmenities(property.getAmenities());
                    });

                    //rules
                    List<Rule> displayedRules = property.getRules().size() > 5 ? property.getRules().subList(0, 5) : property.getRules();
                    rvRules.setAdapter(new RulesAdapter(displayedRules));
                    if (property.getRules().size()>5){
                        view_all_rules.setVisibility(View.VISIBLE);
                    }
                    else{
                        view_all_rules.setVisibility(View.GONE);
                    }
                    view_all_rules.setOnClickListener(view -> {
                        showAllPropertyRules(property.getRules());
                    });

                    //landmarks
                    List<Landmark> displayedLandmark = property.getLandmarks().size() > 5 ? property.getLandmarks().subList(0, 5) : property.getLandmarks();
                    rvLandmarks.setAdapter(new LandmarksAdapter(displayedLandmark));
                    if (property.getLandmarks().size()>5){
                        view_all_landmarks.setVisibility(View.VISIBLE);
                    }
                    else{
                        view_all_landmarks.setVisibility(View.GONE);
                    }
                    view_all_landmarks.setOnClickListener(view -> {
                        showAllPropertyLandmarks(property.getLandmarks());
                    });

                    //similar properties
                    rvSimilarProperties.setAdapter(new SimilarPropertiesAdapter(getApplicationContext(),property.getSimilarProperties()));

                    section_content.setVisibility(View.VISIBLE);
                    section_skleton.setVisibility(View.GONE);
                    section_error.setVisibility(View.GONE);
                    section_skeleton_footer_price.setVisibility(View.GONE);
                    section_footer_price.setVisibility(View.VISIBLE);
                    tv_property_minimum_price.setText(property_minimum_price);
                    tv_currency.setText(price_currency);

                } else {
                    Log.e("ERROR", "Response failed");
                    section_content.setVisibility(View.GONE);
                    section_skleton.setVisibility(View.GONE);
                    section_skeleton_footer_price.setVisibility(View.GONE);
                    section_footer_price.setVisibility(View.GONE);
                    section_error.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<PropertyDetailsResponse> call, Throwable t) {
                Log.e("ERROR", "Failed to fetch property", t);
                section_content.setVisibility(View.GONE);
                section_skleton.setVisibility(View.GONE);
                section_skeleton_footer_price.setVisibility(View.GONE);
                section_footer_price.setVisibility(View.GONE);
                section_error.setVisibility(View.VISIBLE);
            }
        });
    }

    private void displayBookingInfo(String checkinDate,String checkoutDate,String nb_adultes,String nb_enfants,TextView tvBookingInfoDate,TextView tvBookingInfoGuest){

        int sum=Integer.valueOf(nb_adultes)+Integer.valueOf(nb_enfants);
        if (sum>1){
            tvBookingInfoGuest.setText(String.valueOf(sum)+" "+"visiteurs");
        }
        else {
            tvBookingInfoGuest.setText(String.valueOf(sum)+" "+"visiteur");
        }

        tvBookingInfoDate.setText(checkinDate+" - "+checkoutDate);

    }


    @Override
    public void onModifyButtonClicked(String city_or_property_response, String checkinDate_response, String checkoutDate_response, String checkinDateFrenchFormat_response, String checkoutDateFrenchFormat_response, int adultesNumber_response, int childrenNumber_response) {

        displayBookingInfo(checkinDateFrenchFormat_response,checkoutDateFrenchFormat_response,String.valueOf(adultesNumber_response),String.valueOf(childrenNumber_response),tvBookingInfoDate,tvBookingInfoGuest);
    }

    @Override
    public void onDialogFragmentDismiss() {

    }

    private void showAllPropertyAmenities(List<PropertyAmenity> amenities) {
        PropertyAmenitiesDialogFragment propertyAmenitiesDialogFragment = new PropertyAmenitiesDialogFragment (amenities);
        propertyAmenitiesDialogFragment.show(getSupportFragmentManager(), propertyAmenitiesDialogFragment.getTag());
    }
    private void showAllPropertyRules(List<Rule> rules) {
        PropertyRulesDialogFragment propertyRulesDialogFragment = new PropertyRulesDialogFragment (rules);
        propertyRulesDialogFragment.show(getSupportFragmentManager(), propertyRulesDialogFragment.getTag());
    }
    private void showAllPropertyLandmarks(List<Landmark> landmarks) {
        PropertyLandmarksDialogFragment propertyLandmarksDialogFragment = new PropertyLandmarksDialogFragment (landmarks);
        propertyLandmarksDialogFragment.show(getSupportFragmentManager(), propertyLandmarksDialogFragment.getTag());
    }

    private void openPropertyRoomListActivity(String property_id,String property_name) {
        Intent intent = new Intent(this, PropertyRoomListActivity.class);
        intent.putExtra("property_type", property_type);
        intent.putExtra("checkinDate", checkinDate);
        intent.putExtra("checkoutDate", checkoutDate);
        intent.putExtra("checkinDateFrench", checkinDateFrench);
        intent.putExtra("checkoutDateFrench", checkoutDateFrench);
        intent.putExtra("city_or_property", city_or_property);
        intent.putExtra("nb_adultes", nb_adultes);
        intent.putExtra("nb_enfants",nb_enfants);
        intent.putExtra("property_id", property_id);
        intent.putExtra("property_name", property_name);

        intent.putExtra("property_adress", property_adress);
        intent.putExtra("property_first_image", property_first_image);

        startActivity(intent);
    }
}