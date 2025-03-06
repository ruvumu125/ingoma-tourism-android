package com.ingoma.tourism;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowInsets;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.ingoma.tourism.constant.Constant;
import com.ingoma.tourism.model.HotelModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ConfirmHotelBookingActivity extends AppCompatActivity {

    private String plan_name,plan_description,room_name,room_size,room_bed_type,room_main_image,property_id,property_name,property_adress,property_first_image,property_type,checkinDate,checkoutDate,checkinDateFrench,checkoutDateFrench,city_or_property,nb_adultes,nb_enfants;
    private LinearLayout continue_btn;
    private TextView tv_booking_info,txtStartDate,txtEndDate,txtNight;
    private TextView tv_property_type,tvPropertyName,tvPropertyAddress,tvRooName,tvPlanName,tvPlanDescription;
    private AppCompatImageView propertyImageView,roomImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_hotel_booking);

        //padding status bar and bottom navigation bar
        View RootLayout = findViewById(R.id.cordinatorLyt);
        View toolbar = findViewById(R.id.htab_appbar);
        paddingStatusBar(toolbar);
        paddingBottomNavigationBar(RootLayout);

        // initialisation
        tv_booking_info = findViewById(R.id.toolbar_custom_sub_title);
        txtStartDate = findViewById(R.id.txtStartDate);
        txtEndDate = findViewById(R.id.txtEndDate);
        txtNight = findViewById(R.id.txtNight);
        continue_btn = findViewById(R.id.lyt_cta);


        // Get default dates from hotel list activity
        Intent intent = getIntent();
        if (intent != null) {

            property_type = intent.getStringExtra("property_type");
            property_id = intent.getStringExtra("property_id");
            property_name = intent.getStringExtra("property_name");
            property_adress = intent.getStringExtra("property_adress");
            property_first_image = intent.getStringExtra("property_first_image");

            checkinDate = intent.getStringExtra("checkinDate");
            checkoutDate = intent.getStringExtra("checkoutDate");
            checkinDateFrench = intent.getStringExtra("checkinDateFrench");
            checkoutDateFrench = intent.getStringExtra("checkoutDateFrench");
            city_or_property = intent.getStringExtra("city_or_property");
            nb_adultes = intent.getStringExtra("nb_adultes");
            nb_enfants = intent.getStringExtra("nb_enfants");

            room_name= intent.getStringExtra("room_name");
            room_size= intent.getStringExtra("room_size");
            room_bed_type = intent.getStringExtra("room_bed_type");
            room_main_image= intent.getStringExtra("room_main_image");

            plan_name= intent.getStringExtra("plan_name");
            plan_description= intent.getStringExtra("plan_description");


            String guest_info=displayGuestInfo(property_type,nb_adultes,nb_enfants);
            tv_booking_info.setText(checkinDateFrench+" - "+checkoutDateFrench+guest_info);
            txtStartDate.setText(checkinDateFrench);
            txtEndDate.setText(checkoutDateFrench);

            String nbNight="";
            if (getDaysBetween(checkinDate,checkoutDate)>1){
                nbNight=String.valueOf(getDaysBetween(checkinDate,checkoutDate))+" "+"nuits";
            }
            else {
                nbNight=String.valueOf(getDaysBetween(checkinDate,checkoutDate))+" "+"nuit";
            }
            txtNight.setText(nbNight);

            //autres data
            tv_property_type.setText(property_type);
            tvPropertyName.setText(property_name);
            tvPropertyAddress.setText(property_adress);
            tvRooName.setText(room_name);
            tvPlanName.setText(plan_name);
            tvPlanDescription.setText(plan_description);

            //images
            String baseUrlProperty = Constant.BASE_URL + "api/v1/property-image/";
            String fullImageUrlProperty = baseUrlProperty+ property_first_image;

            String baseUrlRoom = Constant.BASE_URL + "api/v1/room-image/";
            String fullImageUrlRoom = baseUrlRoom+ room_name;

            Glide.with(this)
                    .load(fullImageUrlProperty)
                    .placeholder(R.drawable.hotel_place_holder)
                    .into(propertyImageView);

            Glide.with(this)
                    .load(fullImageUrlRoom)
                    .placeholder(R.drawable.hotel_place_holder)
                    .into(roomImageView);



        }
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

        return statusBarHeight;
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

    public static long getDaysBetween(String date1, String date2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Define date format

        try {
            Date startDate = sdf.parse(date1);
            Date endDate = sdf.parse(date2);

            // Calculate the difference in milliseconds
            long diffInMillis = endDate.getTime() - startDate.getTime();

            // Convert milliseconds to days
            return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
            return -1; // Return -1 if there's an error
        }
    }


}