package com.ingoma.tourism;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ingoma.tourism.adapter.LocationSearchAdapter;
import com.ingoma.tourism.api.LocationSearchService;
import com.ingoma.tourism.api.Retrofit2Client;
import com.ingoma.tourism.model.LocationSearch;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationSearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LocationSearchAdapter locationSearchAdapter;
    private List<LocationSearch> cityList;
    private AppCompatEditText et_search;

    private LocationSearchService locationSearchService;
    private Retrofit2Client retrofit2Client;
    private TextView tv_popular_locations;
    private LinearLayout shimmer_section,error_section;
    private String property_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);

        //padding status bar and bottom navigation bar
        View toolbar = findViewById(R.id.constraintLayout);
        paddingStatusBar(toolbar);

        // Get property type from home fragement activity
        Intent intent = getIntent();
        property_type = intent.getStringExtra("property_type");

        retrofit2Client = new Retrofit2Client(getApplicationContext());
        locationSearchService = retrofit2Client.createService(LocationSearchService.class);

        recyclerView = findViewById(R.id.rv_location);
        shimmer_section = findViewById(R.id.shimmer_section);
        error_section = findViewById(R.id.error_section);
        et_search = findViewById(R.id.et_search);
        tv_popular_locations = findViewById(R.id.header);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        locationSearchAdapter = new LocationSearchAdapter();
        recyclerView.setAdapter(locationSearchAdapter);

        // Fetch default cities (without query)
        fetchSearchResults(property_type,"");

        // Listen for text input in search field
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length()>0){
                    tv_popular_locations.setVisibility(View.GONE);
                }
                else{
                    tv_popular_locations.setVisibility(View.VISIBLE);
                }
                fetchSearchResults(property_type,s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

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

    private void fetchSearchResults(String propertyType, String query) {

        shimmer_section.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        error_section.setVisibility(View.GONE);

        Call<List<LocationSearch>> call = locationSearchService.searchLocation(query, propertyType);

        call.enqueue(new Callback<List<LocationSearch>>() {
            @Override
            public void onResponse(Call<List<LocationSearch>> call, Response<List<LocationSearch>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    locationSearchAdapter.setSearchResults(response.body(),city -> {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("selected_city", city.getName());
                        setResult(RESULT_OK, resultIntent);
                        finish(); // Close CityListActivity and return result
                    });

                    shimmer_section.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    error_section.setVisibility(View.GONE);

                } else {

                    shimmer_section.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    tv_popular_locations.setVisibility(View.GONE);
                    error_section.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<LocationSearch>> call, Throwable t) {

                shimmer_section.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                tv_popular_locations.setVisibility(View.GONE);
                error_section.setVisibility(View.VISIBLE);
            }
        });
    }

}