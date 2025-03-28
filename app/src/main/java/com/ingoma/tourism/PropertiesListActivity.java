package com.ingoma.tourism;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowInsets;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ingoma.tourism.adapter.AmenityAdapter;
import com.ingoma.tourism.adapter.PropertyListAdapter;
import com.ingoma.tourism.api.PropertyApiService;
import com.ingoma.tourism.api.Retrofit2Client;
import com.ingoma.tourism.dialog.EditBookingInfoDialogFragment;
import com.ingoma.tourism.dialog.PropertyFilterDialogFragment;
import com.ingoma.tourism.dialog.PropertyPriceFilterDialogFragment;
import com.ingoma.tourism.dialog.PropertySortDialogFragment;
import com.ingoma.tourism.model.Amenity;
import com.ingoma.tourism.model.PropertyList;
import com.ingoma.tourism.model.PropertyListResponse;
import com.ingoma.tourism.model.Sort;
import com.ingoma.tourism.utils.LoginPreferencesManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropertiesListActivity extends AppCompatActivity implements EditBookingInfoDialogFragment.CallBackListener,PropertyFilterDialogFragment.OnAmenitiesSelectedListener,PropertySortDialogFragment.OnSortSelectedListener {

    private LinearLayout Ll_sort,Ll_filter,Ll_price;

    private RecyclerView hotelRecyclerView;
    private TextView toolbar_custom_title,tv_date_guest;
    private LinearLayout Ll_date_guest_infos;
    private ImageView imgEditArrow;

    private String tarification_type,type_search,property_type,checkinDate,checkoutDate,checkinDateFrench,checkoutDateFrench,city_or_property,nb_adultes,nb_enfants;
    private boolean isLoading = false;
    private int currentPage = 1;
    private int totalPage = 1;
    private int pageSize = 10;
    private List<PropertyList> hotelList = new ArrayList<>();
    private PropertyListAdapter hotelAdapter;


    private Retrofit2Client retrofit2Client;
    private PropertyApiService propertyApiService;

    private NestedScrollView skletonPrincipale;
    private LinearLayout skletonFiltres,section_trier_filtres_prix,error_section;
    private boolean isArrowDown = true;
    //private List<Amenity> theSelectedAmenities = new ArrayList<>();
    private List<Amenity> selectedAmenities = new ArrayList<>();
    private Sort selectedSort; // Store last selected sort

    private Float selectedMinimumPriceFilter=null;
    private Float selectedMaximumPriceFilter=null;
    private List<Integer> selectedAmenitiesFilter=new ArrayList<>();
    private String selectedSortFilter="";

    private ImageView icon_top_right_trier,icon_top_right_filtrer,icon_top_right_prix;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_properties_list);

        retrofit2Client=new Retrofit2Client(getApplicationContext());
        propertyApiService = retrofit2Client.createService(PropertyApiService.class);

        //padding status bar and bottom navigation bar
        View RootLayout = findViewById(R.id.detail_activity_lyt);
        View toolbar = findViewById(R.id.htab_appbar);
        paddingStatusBar(toolbar);
        paddingBottomNavigationBar(RootLayout);

        //initialize views
        toolbar_custom_title=findViewById(R.id.toolbar_custom_title);
        tv_date_guest=findViewById(R.id.tv_date_guest);
        Ll_date_guest_infos=findViewById(R.id.Ll_date_guest_infos);
        imgEditArrow=findViewById(R.id.imgEditArrow);
        skletonFiltres=findViewById(R.id.skletonFiltres);
        skletonPrincipale=findViewById(R.id.skletonPrincipale);
        section_trier_filtres_prix=findViewById(R.id.section_trier_filtres_prix);
        error_section=findViewById(R.id.error_section);

        Ll_sort=findViewById(R.id.layout_trier);
        Ll_filter=findViewById(R.id.layout_filtrer);
        Ll_price=findViewById(R.id.layout_prix);

        icon_top_right_trier=findViewById(R.id.icon_top_right_trier);
        icon_top_right_filtrer=findViewById(R.id.icon_top_right_filtrer);
        icon_top_right_prix=findViewById(R.id.icon_top_right_prix);

        // Get default dates from hotel search activity
        Intent intent = getIntent();
        checkinDate = intent.getStringExtra("checkinDate");
        checkoutDate = intent.getStringExtra("checkoutDate");
        checkinDateFrench = intent.getStringExtra("checkinDateFrench");
        checkoutDateFrench = intent.getStringExtra("checkoutDateFrench");
        city_or_property = intent.getStringExtra("city_or_property");
        type_search = intent.getStringExtra("type");
        nb_adultes = intent.getStringExtra("nb_adultes");
        nb_enfants = intent.getStringExtra("nb_enfants");
        property_type = intent.getStringExtra("property_type");
        tarification_type = intent.getStringExtra("tarification_type");

        //set textview
        toolbar_custom_title.setText(city_or_property);
        String guest_info=displayGuestInfo(property_type,nb_adultes,nb_enfants);
        tv_date_guest.setText(checkinDateFrench+" - "+checkoutDateFrench+guest_info);


        // Initialize RecyclerView
        hotelRecyclerView = findViewById(R.id.rvSrpList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        hotelRecyclerView.setLayoutManager(layoutManager);

        hotelAdapter = new PropertyListAdapter(this, hotelList,property -> {
            openPropertyDetailsActivity(property);
        });
        hotelRecyclerView.setAdapter(hotelAdapter);

        fetchProperties(property_type, city_or_property,selectedSortFilter,selectedMinimumPriceFilter,selectedMaximumPriceFilter, selectedAmenitiesFilter,pageSize,currentPage);

        hotelRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {  // Scrolling down
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    if (!isLoading && currentPage < totalPage) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                            loadMoreHotels();
                        }
                    }
                }
            }
        });


        Ll_date_guest_infos.setOnClickListener(view -> {

            if (isArrowDown) {
                imgEditArrow.setImageResource(R.drawable.htl_ic_arrow_up_theme); // Change to another image
            }

            EditBookingInfoDialogFragment editBookingInfoDialogFragment = new EditBookingInfoDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString("city_or_property", city_or_property);
            bundle.putString("checkinDate", checkinDate);
            bundle.putString("checkoutDate", checkoutDate);
            bundle.putString("nb_adultes", nb_adultes);
            bundle.putString("nb_enfants", nb_enfants);
            bundle.putString("property_type", property_type);
            bundle.putString("provenance", "property_listing_activity");
            editBookingInfoDialogFragment.setArguments(bundle);
            editBookingInfoDialogFragment.show(getSupportFragmentManager(), "EditBookingInfoBottomSheetDialog");
        });


        Ll_sort.setOnClickListener(view -> {
            PropertySortDialogFragment propertySortDialogFragment = PropertySortDialogFragment.newInstance(selectedSort);
            propertySortDialogFragment.show(getSupportFragmentManager(), "PropertySortBottomSheetDialog");
        });

        Ll_filter.setOnClickListener(view -> {

            PropertyFilterDialogFragment propertyFilterDialogFragment = PropertyFilterDialogFragment.newInstance(type_search,city_or_property,property_type,new ArrayList<>(selectedAmenities));
            propertyFilterDialogFragment.show(getSupportFragmentManager(), "PropertyFilterBottomSheetDialog");
        });

        Ll_price.setOnClickListener(view -> {

            Float minimumPrice;
            Float maxmumPrice;

            if (selectedMinimumPriceFilter == null){
                minimumPrice=0.0f;
            }
            else {
                minimumPrice=selectedMinimumPriceFilter;
            }

            if (selectedMaximumPriceFilter == null){
                maxmumPrice=0.0f;
            }
            else {
                maxmumPrice=selectedMaximumPriceFilter;
            }

            PropertyPriceFilterDialogFragment propertyPriceFilterDialogFragment = PropertyPriceFilterDialogFragment.newInstance(type_search,city_or_property,property_type,minimumPrice,maxmumPrice);
            propertyPriceFilterDialogFragment.setOnPriceRangeSelectedListener((minValue, maxValue) -> {

                selectedMinimumPriceFilter=minValue;
                selectedMaximumPriceFilter=maxValue;

                hotelAdapter.clearData();
                fetchProperties(property_type, city_or_property,selectedSortFilter,selectedMinimumPriceFilter,selectedMaximumPriceFilter, selectedAmenitiesFilter,pageSize,currentPage);

                SortFilterPriceAppliedNotifier();
            });
            propertyPriceFilterDialogFragment.show(getSupportFragmentManager(), "PropertyPriceFilterBottomSheetDialog");

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    // Get properties data
    private void fetchProperties(String propertyType, String search,String sortBy,Float minPrice, Float maxPrice, List<Integer> amenities,int perPage,int page) {
        isLoading = true;
        skletonFiltres.setVisibility(View.VISIBLE);
        skletonPrincipale.setVisibility(View.VISIBLE);
        section_trier_filtres_prix.setVisibility(View.GONE);
        error_section.setVisibility(View.GONE);

        propertyApiService.listing(propertyType,search,sortBy,minPrice,maxPrice,amenities,perPage,page).enqueue(new Callback<PropertyListResponse>() {
            @Override
            public void onResponse(Call<PropertyListResponse> call, Response<PropertyListResponse> response) {
                isLoading = false;
                if (response.isSuccessful() && response.body() != null) {

                    skletonFiltres.setVisibility(View.GONE);
                    skletonPrincipale.setVisibility(View.GONE);
                    section_trier_filtres_prix.setVisibility(View.VISIBLE);
                    hotelRecyclerView.setVisibility(View.VISIBLE);
                    error_section.setVisibility(View.GONE);

                    totalPage = response.body().getPagination().getTotal_pages();
                    hotelList.addAll(response.body().getData());
                    hotelAdapter.notifyDataSetChanged();


                } else {

                    skletonFiltres.setVisibility(View.GONE);
                    skletonPrincipale.setVisibility(View.GONE);
                    section_trier_filtres_prix.setVisibility(View.GONE);
                    hotelRecyclerView.setVisibility(View.GONE);
                    error_section.setVisibility(View.VISIBLE);
                    //Toast.makeText(PropertiesListActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PropertyListResponse> call, Throwable t) {
                isLoading = false;

                skletonFiltres.setVisibility(View.GONE);
                skletonPrincipale.setVisibility(View.GONE);
                section_trier_filtres_prix.setVisibility(View.GONE);
                hotelRecyclerView.setVisibility(View.GONE);
                error_section.setVisibility(View.VISIBLE);

                //Log.e("API_ERROR", "Error fetching data: " + t.getMessage());
                //Toast.makeText(PropertiesListActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMoreHotels() {
        isLoading = true;
        hotelAdapter.showLoading();
        new Handler().postDelayed(() -> {
            currentPage++;
            propertyApiService.listing(property_type, city_or_property,selectedSortFilter,selectedMinimumPriceFilter,selectedMaximumPriceFilter, selectedAmenitiesFilter,pageSize,currentPage).enqueue(new Callback<PropertyListResponse>() {
                @Override
                public void onResponse(Call<PropertyListResponse> call, Response<PropertyListResponse> response) {
                    hotelAdapter.hideLoading();
                    isLoading = false;
                    if (response.isSuccessful() && response.body() != null) {
                        hotelAdapter.addHotels(response.body().getData());
                    }
                }

                @Override
                public void onFailure(Call<PropertyListResponse> call, Throwable t) {
                    hotelAdapter.hideLoading();
                    isLoading = false;
                    Log.e("API_ERROR", "Error fetching data: " + t.getMessage());

                    skletonFiltres.setVisibility(View.GONE);
                    skletonPrincipale.setVisibility(View.GONE);
                    section_trier_filtres_prix.setVisibility(View.GONE);
                    hotelRecyclerView.setVisibility(View.GONE);
                    error_section.setVisibility(View.VISIBLE);
                }
            });
        }, 2000);
    }


    @Override
    public void onModifyButtonClicked(String search_type,String city_or_property_response, String checkinDate_response, String checkoutDate_response, String checkinDateFrenchFormat_response, String checkoutDateFrenchFormat_response, int adultesNumber_response, int childrenNumber_response) {

        type_search=search_type;
        checkinDate=checkinDate_response;
        checkoutDate=checkoutDate_response;
        checkinDateFrench=checkinDateFrenchFormat_response;
        checkoutDateFrench=checkoutDateFrenchFormat_response;
        city_or_property=city_or_property_response;
        nb_adultes=String.valueOf(adultesNumber_response);
        nb_enfants=String.valueOf(childrenNumber_response);
        tarification_type=getTarificationType(checkinDate_response,checkoutDate_response);

        //set textview
        toolbar_custom_title.setText(city_or_property_response);
        String guest_info=displayGuestInfo(property_type,String.valueOf(adultesNumber_response),String.valueOf(childrenNumber_response));
        tv_date_guest.setText(checkinDateFrench+" - "+checkoutDateFrench+", "+guest_info);

        imgEditArrow.setImageResource(R.drawable.htl_ic_arrow_down_theme);

        //update recyclerview
        hotelAdapter.clearData();
        currentPage=1;
        fetchProperties(property_type, city_or_property,selectedSortFilter,selectedMinimumPriceFilter,selectedMaximumPriceFilter, selectedAmenitiesFilter,pageSize,currentPage);

        SortFilterPriceAppliedNotifier();


    }

    @Override
    public void onDialogFragmentDismiss() {
        imgEditArrow.setImageResource(R.drawable.htl_ic_arrow_down_theme);
    }

    private void openPropertyDetailsActivity(PropertyList hotelModel) {
        Intent intent = new Intent(this, PropertiesDetailsActivity.class);
        intent.putExtra("property_type", property_type);
        intent.putExtra("checkinDate", checkinDate);
        intent.putExtra("checkoutDate", checkoutDate);
        intent.putExtra("checkinDateFrench", checkinDateFrench);
        intent.putExtra("checkoutDateFrench", checkoutDateFrench);
        intent.putExtra("city_or_property", city_or_property);
        intent.putExtra("nb_adultes", nb_adultes);
        intent.putExtra("nb_enfants",nb_enfants);
        intent.putExtra("property_id", String.valueOf(hotelModel.getId()));
        intent.putExtra("property_name", hotelModel.getName());
        intent.putExtra("property_minimum_price", String.valueOf(hotelModel.getMinPrice()));
        intent.putExtra("price_currency", hotelModel.getCurrency());

        intent.putExtra("property_adress", hotelModel.getAddress());
        intent.putExtra("property_first_image", hotelModel.getImages().get(0));
        intent.putExtra("tarification_type", tarification_type);

        startActivity(intent);
    }

    @Override
    public void onAmenitiesSelected(List<Amenity> selectedAmenities) {

        this.selectedAmenities = selectedAmenities;
        for (Amenity amenity : selectedAmenities) {
            selectedAmenitiesFilter.add(Integer.valueOf(amenity.getId()));
        }
        hotelAdapter.clearData();
        fetchProperties(property_type, city_or_property,selectedSortFilter,selectedMinimumPriceFilter,selectedMaximumPriceFilter, selectedAmenitiesFilter,pageSize,currentPage);

        SortFilterPriceAppliedNotifier();

    }

    @Override
    public void onSortSelected(Sort sort) {
        selectedSort = sort;
        if (sort.getName().equals("Prix le plus bas")){
            selectedSortFilter="price_low_to_high";
        }
        else {
            selectedSortFilter="price_high_to_low";
        }
        hotelAdapter.clearData();
        fetchProperties(property_type, city_or_property,selectedSortFilter,selectedMinimumPriceFilter,selectedMaximumPriceFilter, selectedAmenitiesFilter,pageSize,currentPage);

        SortFilterPriceAppliedNotifier();

    }

    private void SortFilterPriceAppliedNotifier(){

        if (selectedSortFilter.equals("")){
            icon_top_right_trier.setVisibility(View.GONE);
        }
        else{
            icon_top_right_trier.setVisibility(View.VISIBLE);

        }

        if (selectedMinimumPriceFilter == null && selectedMaximumPriceFilter == null){
            icon_top_right_prix.setVisibility(View.GONE);

        }
        else{
            icon_top_right_prix.setVisibility(View.VISIBLE);

        }

        if (selectedAmenitiesFilter.isEmpty()){
            icon_top_right_filtrer.setVisibility(View.GONE);

        }
        else{
            icon_top_right_filtrer.setVisibility(View.VISIBLE);

        }

    }

    public static String getTarificationType(String dateStr1, String dateStr2) {
        // Define the date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Parse the strings into LocalDate
        LocalDate date1 = LocalDate.parse(dateStr1, formatter);
        LocalDate date2 = LocalDate.parse(dateStr2, formatter);

        // Calculate the difference in days
        long daysBetween = ChronoUnit.DAYS.between(date1, date2);

        // Return "Daily" if under a month, otherwise "Monthly"
        return Math.abs(daysBetween) < 30 ? "daily" : "monthly";
    }

}