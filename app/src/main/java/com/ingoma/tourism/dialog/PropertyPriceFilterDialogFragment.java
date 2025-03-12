package com.ingoma.tourism.dialog;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.textfield.TextInputEditText;
import com.ingoma.tourism.R;
import com.ingoma.tourism.api.AmenityApiService;
import com.ingoma.tourism.api.PriceRangeApiService;
import com.ingoma.tourism.api.Retrofit2Client;
import com.ingoma.tourism.model.Amenity;
import com.ingoma.tourism.model.PriceRangeResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PropertyPriceFilterDialogFragment extends BottomSheetDialogFragment {

    private TextInputEditText minValueEditText;
    private TextInputEditText maxValueEditText;
    private RangeSlider rangeSlider;

    private PriceRangeApiService priceRangeApiService;
    private Retrofit2Client retrofit2Client;
    private LinearLayout section_skleton,section_error,section_content;
    private TextView btnAction;

    private OnPriceRangeSelectedListener listener;

    // Interface to send data to the Activity
    public interface OnPriceRangeSelectedListener {
        void onPriceRangeSelected(float minValue,float maxValue);
    }

    // Method to set the listener
    public void setOnPriceRangeSelectedListener(OnPriceRangeSelectedListener listener) {
        this.listener = listener;
    }


    public static PropertyPriceFilterDialogFragment newInstance(String type,String city_or_property,String property_type) {
        PropertyPriceFilterDialogFragment fragment = new PropertyPriceFilterDialogFragment();
        Bundle args = new Bundle();
        args.putString("type", type);
        args.putString("city_or_property", city_or_property);
        args.putString("property_type", property_type);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View root=inflater.inflate(R.layout.fragment_property_price_filter_dialog, container, false);

        // Initialize RangeSlider
        rangeSlider = root.findViewById(R.id.rangeSlider);
        minValueEditText = root.findViewById(R.id.minValueEditText);
        maxValueEditText = root.findViewById(R.id.maxValueEditText);
        btnAction = root.findViewById(R.id.btnActionPrice);

        section_skleton= root.findViewById(R.id.sl_sect);
        section_error=root.findViewById(R.id.err_sect);
        section_content=root.findViewById(R.id.ct_sect);
        retrofit2Client=new Retrofit2Client(getContext());
        priceRangeApiService = retrofit2Client.createService(PriceRangeApiService.class);
        rangeSlider.setTrackActiveTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.primary)));
        rangeSlider.setTrackInactiveTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.app_color_icon_default)));


        if (getArguments() != null) {
            String type = getArguments().getString("type");
            String city_or_property = getArguments().getString("city_or_property", "");
            String property_type = getArguments().getString("property_type", "");

            if (type.equals("city")){
                //adapter.clearData();
                fetchPriceRangeByPropertyTypeAndCityName(property_type, city_or_property,rangeSlider);
            }
            else {
                //adapter.clearData();
                fetchPriceRangeByPropertyTypeAndPropertyName(property_type, city_or_property,rangeSlider);            }
        }

        // Update EditTexts when RangeSlider values change
        rangeSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(RangeSlider slider, float value, boolean fromUser) {
                List<Float> values = slider.getValues();
                if (values.size() >= 2) {
                    float minValue = values.get(0);
                    float maxValue = values.get(1);
                    minValueEditText.setText(String.valueOf((int) minValue));
                    maxValueEditText.setText(String.valueOf((int) maxValue));
                }
            }
        });

        // Update RangeSlider when Min EditText value changes
        minValueEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    float minValue = Float.parseFloat(s.toString());
                    float stepSize = rangeSlider.getStepSize(); // Get stepSize
                    float validMinValue = Math.round(minValue / stepSize) * stepSize; // Round to nearest stepSize
                    validMinValue = Math.max(validMinValue, rangeSlider.getValueFrom()); // Ensure >= valueFrom
                    validMinValue = Math.min(validMinValue, rangeSlider.getValues().get(1)); // Ensure <= current maxValue

                    // Update RangeSlider with valid value
                    rangeSlider.setValues(validMinValue, rangeSlider.getValues().get(1));

                    // Update EditText with valid value
                    minValueEditText.removeTextChangedListener(this); // Prevent infinite loop
                    minValueEditText.setText(String.valueOf((int) validMinValue));
                    minValueEditText.setSelection(minValueEditText.getText().length()); // Move cursor to end
                    minValueEditText.addTextChangedListener(this); // Reattach listener
                } catch (NumberFormatException e) {
                    // Handle invalid input
                }
            }
        });

        // Update RangeSlider when Max EditText value changes
        maxValueEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    float maxValue = Float.parseFloat(s.toString());
                    float stepSize = rangeSlider.getStepSize(); // Get stepSize
                    float validMaxValue = Math.round(maxValue / stepSize) * stepSize; // Round to nearest stepSize
                    validMaxValue = Math.min(validMaxValue, rangeSlider.getValueTo()); // Ensure <= valueTo
                    validMaxValue = Math.max(validMaxValue, rangeSlider.getValues().get(0)); // Ensure >= current minValue

                    // Update RangeSlider with valid value
                    rangeSlider.setValues(rangeSlider.getValues().get(0), validMaxValue);

                    // Update EditText with valid value
                    maxValueEditText.removeTextChangedListener(this); // Prevent infinite loop
                    maxValueEditText.setText(String.valueOf((int) validMaxValue));
                    maxValueEditText.setSelection(maxValueEditText.getText().length()); // Move cursor to end
                    maxValueEditText.addTextChangedListener(this); // Reattach listener
                } catch (NumberFormatException e) {
                    // Handle invalid input
                }
            }
        });

        btnAction.setOnClickListener(view -> {
            if (listener != null) {
                listener.onPriceRangeSelected(Float.parseFloat(minValueEditText.getText().toString()),Float.parseFloat(maxValueEditText.getText().toString()));
            }
            dismiss(); // Close the bottom sheet
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

    private void fetchPriceRangeByPropertyTypeAndCityName(String propertyType, String cityName,RangeSlider slider) {

        section_skleton.setVisibility(View.VISIBLE);
        section_error.setVisibility(View.GONE);
        section_content.setVisibility(View.GONE);

        priceRangeApiService.getPriceRangeByTypeAndCityName(propertyType, cityName)
                .enqueue(new Callback<PriceRangeResponse>() {
                    @Override
                    public void onResponse(Call<PriceRangeResponse> call, Response<PriceRangeResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            PriceRangeResponse priceRange = response.body();
                            double minPrice = priceRange.getMinPrice();
                            double maxPrice = priceRange.getMaxPrice();

                            // Set default values in the RangeSlider
                            slider.setValueFrom((float) minPrice);
                            slider.setValueTo((float) maxPrice);
                            slider.setStepSize(1);
                            slider.setValues((float) minPrice, (float) maxPrice);

                            section_skleton.setVisibility(View.GONE);
                            section_error.setVisibility(View.GONE);
                            section_content.setVisibility(View.VISIBLE);

                        } else {

                            section_skleton.setVisibility(View.GONE);
                            section_error.setVisibility(View.VISIBLE);
                            section_content.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<PriceRangeResponse> call, Throwable t) {
                        Log.e("API Error", t.getMessage());
                        section_skleton.setVisibility(View.GONE);
                        section_error.setVisibility(View.VISIBLE);
                        section_content.setVisibility(View.GONE);
                    }
                });
    }

    private void fetchPriceRangeByPropertyTypeAndPropertyName(String propertyType, String propertyName,RangeSlider slider) {

        section_skleton.setVisibility(View.VISIBLE);
        section_error.setVisibility(View.GONE);
        section_content.setVisibility(View.GONE);

        priceRangeApiService.getPriceRangeByTypeAndPropertyName(propertyType, propertyName)
                .enqueue(new Callback<PriceRangeResponse>() {
                    @Override
                    public void onResponse(Call<PriceRangeResponse> call, Response<PriceRangeResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            PriceRangeResponse priceRange = response.body();
                            double minPrice = priceRange.getMinPrice();
                            double maxPrice = priceRange.getMaxPrice();

                            // Set default values in the RangeSlider
                            slider.setValueFrom((float) minPrice);
                            slider.setValueTo((float) maxPrice);
                            slider.setStepSize(1);
                            slider.setValues((float) minPrice, (float) maxPrice);

                            section_skleton.setVisibility(View.GONE);
                            section_error.setVisibility(View.GONE);
                            section_content.setVisibility(View.VISIBLE);

                        } else {

                            section_skleton.setVisibility(View.GONE);
                            section_error.setVisibility(View.VISIBLE);
                            section_content.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<PriceRangeResponse> call, Throwable t) {

                        section_skleton.setVisibility(View.VISIBLE);
                        section_error.setVisibility(View.VISIBLE);
                        section_content.setVisibility(View.GONE);
                    }
                });
    }


}