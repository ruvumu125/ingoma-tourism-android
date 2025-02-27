package com.ingoma.tourism.dialog;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.textfield.TextInputEditText;
import com.ingoma.tourism.R;

import java.util.List;


public class PropertyPriceFilterDialogFragment extends BottomSheetDialogFragment {

    private TextInputEditText minValueEditText;
    private TextInputEditText maxValueEditText;
    private RangeSlider rangeSlider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View root=inflater.inflate(R.layout.fragment_property_price_filter_dialog, container, false);

        // Initialize RangeSlider
        rangeSlider = root.findViewById(R.id.rangeSlider);
        minValueEditText = root.findViewById(R.id.minValueEditText);
        maxValueEditText = root.findViewById(R.id.maxValueEditText);


        // Set default values programmatically
        float valueFrom = 0; // Minimum value
        float valueTo = 1000; // Maximum value
        float stepSize = 1; // Step size
        float defaultMinValue = 200; // Default minimum value
        float defaultMaxValue = 800; // Default maximum value

        // Configure RangeSlider
        rangeSlider.setValueFrom(valueFrom);
        rangeSlider.setValueTo(valueTo);
        rangeSlider.setStepSize(stepSize);
        rangeSlider.setValues(defaultMinValue, defaultMaxValue); // Set default range

        rangeSlider.setTrackActiveTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.primary)));
        rangeSlider.setTrackInactiveTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.app_color_icon_default)));

        // Set initial values in EditTexts
        minValueEditText.setText(String.valueOf(defaultMinValue));
        maxValueEditText.setText(String.valueOf(defaultMaxValue));

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
}