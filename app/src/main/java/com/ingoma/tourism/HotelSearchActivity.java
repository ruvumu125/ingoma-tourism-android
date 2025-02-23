package com.ingoma.tourism;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowInsets;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textview.MaterialTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HotelSearchActivity extends AppCompatActivity {

    private TextView tv_toolbar_title;
    private AppCompatImageButton ivBack;
    private LinearLayoutCompat layout_check_in_date,layout_check_out_date;

    private AppCompatTextView checkinDate,checkoutDate;
    private MaterialTextView checkinDay,checkoutDay;

    private String checkinStr,checkoutStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_search);

        // Find TextViews
        checkinDay = findViewById(R.id.tv_check_in_day);
        checkinDate = findViewById(R.id.tv_check_in_date);
        checkoutDay = findViewById(R.id.tv_checkout_day);
        checkoutDate = findViewById(R.id.tv_checkout_date);


        //padding status bar and bottom navigation bar
        View toolbar = findViewById(R.id.customToolbar);
        View yudaya= findViewById(R.id.yudaya);
        paddingStatusBar(toolbar);
        //paddingStatusBar(yudaya);


        // Define the date format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // Get today's date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1); // Move to tomorrow
        checkinStr = sdf.format(calendar.getTime());

        // Get next-next day's date (2 days later from today)
        calendar.add(Calendar.DAY_OF_YEAR, 3); // Move to the day after tomorrow
        checkoutStr = sdf.format(calendar.getTime());

        displayDefaultDates(checkinStr,checkoutStr);




        //date picker
        layout_check_in_date=findViewById(R.id.layout_check_in_date);
        layout_check_out_date=findViewById(R.id.layout_check_out_date);

        layout_check_in_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHotelDatePickerActivity();
            }
        });
        layout_check_out_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHotelDatePickerActivity();
            }
        });

        tv_toolbar_title=(TextView) findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), PropertiesListActivity.class));
            }
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

    private void displayDefaultDates(String startDate,String endDate){

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH);
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.FRENCH); // e.g., Thursday
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM", Locale.FRENCH);

        try {
            // Convert string dates to Date objects
            Date checkin = inputFormat.parse(startDate);
            Date checkout = inputFormat.parse(endDate);

            // Extract day and formatted date
            checkinDay.setText(dayFormat.format(checkin));  // e.g., "Tuesday"
            checkinDate.setText(dateFormat.format(checkin)); // e.g., "25 Feb"
            checkoutDay.setText(dayFormat.format(checkout)); // e.g., "Friday"
            checkoutDate.setText(dateFormat.format(checkout)); // e.g., "28 Feb"

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private final ActivityResultLauncher<Intent> activityResultLauncher =
    registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
                checkinStr = data.getStringExtra("checkInDateResponse");
                checkoutStr = data.getStringExtra("checkOutDateResponse");

                displayDefaultDates(checkinStr,checkoutStr);
            }
        }
    });
    public void openHotelDatePickerActivity() {
        Intent intent = new Intent(this, HotelDatePickerActivity.class);
        intent.putExtra("checkinDate", checkinStr);
        intent.putExtra("checkoutDate", checkoutStr);
        activityResultLauncher.launch(intent);
    }

}