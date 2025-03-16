package com.ingoma.tourism.dialog;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ingoma.tourism.R;
import com.ingoma.tourism.adapter.AmenityAdapter;
import com.ingoma.tourism.api.AmenityApiService;
import com.ingoma.tourism.api.Retrofit2Client;
import com.ingoma.tourism.model.Amenity;
import com.ingoma.tourism.model.AmenityResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PropertyFilterDialogFragment extends BottomSheetDialogFragment  {

    private AmenityApiService amenityApiService;
    private RecyclerView recyclerView;
    private AmenityAdapter adapter;
    private List<Amenity> amenityList= new ArrayList<>();;
    private Retrofit2Client retrofit2Client;
    private LinearLayout section_skleton,section_error;
    private FrameLayout section_listing;
    private TextView btnAction;




    public static PropertyFilterDialogFragment newInstance(String type,String city_or_property,String property_type,ArrayList<Amenity> selectedAmenities) {
        PropertyFilterDialogFragment fragment = new PropertyFilterDialogFragment();
        Bundle args = new Bundle();
        args.putString("type", type);
        args.putString("city_or_property", city_or_property);
        args.putString("property_type", property_type);
        args.putParcelableArrayList("selected_amenities", selectedAmenities);

        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View root=inflater.inflate(R.layout.fragment_property_filter_dialog, container, false);

        recyclerView = root.findViewById(R.id.rvAmenities);
        section_skleton= root.findViewById(R.id.skleton_sect);
        section_error=root.findViewById(R.id.error_sect);
        section_listing=root.findViewById(R.id.flayout);
        btnAction=root.findViewById(R.id.btnAction);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        retrofit2Client=new Retrofit2Client(getContext());
        amenityApiService = retrofit2Client.createService(AmenityApiService.class);

        if (getArguments() != null) {
            String type = getArguments().getString("type","");
            String city_or_property = getArguments().getString("city_or_property", "");
            String property_type = getArguments().getString("property_type", "");
            amenityList = getArguments().getParcelableArrayList("selected_amenities");

            if (type.equals("city")){

                fetchAmenitiesByCity(property_type, city_or_property);
            }
            else{
                fetchAmenitiesByPropertyName(property_type, city_or_property);
            }


        }

        adapter = new AmenityAdapter(getContext(), amenityList);
        recyclerView.setAdapter(adapter);

        // Show selected amenities when button is clicked
        btnAction.setOnClickListener(v -> {

            List<Amenity> selectedAmenities = adapter.getSelectedAmenities();
            sendSelectedAmenities(selectedAmenities);
            dismiss();
        });

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Use ViewTreeObserver to ensure the layout is fully drawn
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                // Remove the listener to ensure it runs only once
                view.getViewTreeObserver().removeOnPreDrawListener(this);

                // Apply the rounded corners after the view is fully drawn
                BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
                if (dialog != null) {
                    View bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                    if (bottomSheet != null) {

                        bottomSheet.setBackgroundResource(R.drawable.bg_shape_top_rounded_day_use_sheets);
                        bottomSheet.setClipToOutline(true);  // Apply rounded corners after view creation
                    }
                }
                return true;
            }
        });
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme);
    }

    @Override
    public void onStart() {
        super.onStart();
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }

            View bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                bottomSheet.setBackgroundResource(R.drawable.bg_shape_top_rounded_day_use_sheets);

                // Ensure the bottomSheet is fully drawn before applying the rounded corners
                bottomSheet.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        bottomSheet.getViewTreeObserver().removeOnPreDrawListener(this);
                        bottomSheet.setClipToOutline(true);  // Apply rounded corners after view is drawn
                        return true;
                    }
                });
            }
        }
    }

    private void fetchAmenitiesByCity(String propertyType, String cityName) {

        section_skleton.setVisibility(View.VISIBLE);
        section_error.setVisibility(View.GONE);
        section_listing.setVisibility(View.GONE);

        amenityApiService.getAmenitiesByCity(propertyType, cityName).enqueue(new Callback<AmenityResponse>() {
            @Override
            public void onResponse(Call<AmenityResponse> call, Response<AmenityResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<Amenity> fetchedAmenities = response.body().getData();
                    // Restore previous selection
                    for (Amenity fetchedAmenity : fetchedAmenities) {
                        for (Amenity selectedAmenity : adapter.getSelectedAmenities()) {
                            if (fetchedAmenity.getId() == selectedAmenity.getId()) {
                                fetchedAmenity.setSelected(true);
                                break;
                            }
                        }
                    }
                    amenityList.clear();
                    amenityList.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();

                    section_skleton.setVisibility(View.GONE);
                    section_error.setVisibility(View.GONE);
                    section_listing.setVisibility(View.VISIBLE);


                } else {
                    section_skleton.setVisibility(View.GONE);
                    section_error.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    btnAction.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<AmenityResponse> call, Throwable t) {
                Log.e("API Error", t.getMessage());

                section_skleton.setVisibility(View.GONE);
                section_error.setVisibility(View.VISIBLE);
                section_listing.setVisibility(View.GONE);
                btnAction.setVisibility(View.GONE);
            }
        });
    }

    private void fetchAmenitiesByPropertyName(String propertyType, String propertyName) {

        section_skleton.setVisibility(View.VISIBLE);
        section_error.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        amenityApiService.getAmenitiesByName(propertyType, propertyName).enqueue(new Callback<AmenityResponse>() {
            @Override
            public void onResponse(Call<AmenityResponse> call, Response<AmenityResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    amenityList.clear();
                    amenityList.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();

                    section_skleton.setVisibility(View.GONE);
                    section_error.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                } else {

                    section_skleton.setVisibility(View.GONE);
                    section_error.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<AmenityResponse> call, Throwable t) {
                Log.e("API Error", t.getMessage());

                section_skleton.setVisibility(View.GONE);
                section_error.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        });
    }

    private void sendSelectedAmenities(List<Amenity> selectedAmenities) {
        if (getActivity() instanceof OnAmenitiesSelectedListener) {
            ((OnAmenitiesSelectedListener) getActivity()).onAmenitiesSelected(selectedAmenities);
        }
    }

    public interface OnAmenitiesSelectedListener {
        void onAmenitiesSelected(List<Amenity> selectedAmenities);
    }
}