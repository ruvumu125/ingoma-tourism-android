package com.ingoma.tourism;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ingoma.tourism.adapter.CityAdapter;
import com.ingoma.tourism.model.City;

import java.util.ArrayList;
import java.util.List;

public class LocationSearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CityAdapter cityAdapter;
    private List<City> cityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);

        //padding status bar and bottom navigation bar
        View toolbar = findViewById(R.id.constraintLayout);
        paddingStatusBar(toolbar);

        recyclerView = findViewById(R.id.rv_location);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cityList = new ArrayList<>();
        cityList.add(new City("New York", 120, R.drawable.ic_htl_has_location));
        cityList.add(new City("Los Angeles", 85, R.drawable.ic_htl_has_location));
        cityList.add(new City("Chicago", 60, R.drawable.recent_search_hotel));
        cityList.add(new City("Houston", 45, R.drawable.recent_search_hotel));

        cityAdapter = new CityAdapter(cityList, city -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selected_city", city.getName());
            setResult(RESULT_OK, resultIntent);
            finish(); // Close CityListActivity and return result
        });

        recyclerView.setAdapter(cityAdapter);

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
}