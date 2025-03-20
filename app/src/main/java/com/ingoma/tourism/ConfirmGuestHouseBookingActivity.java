package com.ingoma.tourism;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.ingoma.tourism.api.BookingService;
import com.ingoma.tourism.api.Retrofit2Client;
import com.ingoma.tourism.constant.Constant;
import com.ingoma.tourism.dialog.SuccessDialogFragment;
import com.ingoma.tourism.model.Booking;
import com.ingoma.tourism.model.User;
import com.ingoma.tourism.model.ValidationErrorResponse;
import com.ingoma.tourism.utils.LoginPreferencesManager;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmGuestHouseBookingActivity extends AppCompatActivity {

    private String property_price,price_currency,property_id,property_name,property_adress,property_first_image,property_type,checkinDate,checkoutDate,checkinDateFrench,checkoutDateFrench,city_or_property,nb_adultes,nb_enfants;
    private TextView tv_booking_info,txtStartDate,txtEndDate,txtNight;
    private TextView tvPropertyName,tvPropertyAddress;
    private AppCompatImageView propertyImageView;
    private TextView unStrikedPrice,txtPerNight;

    private ConstraintLayout btn_next;
    private ConstraintLayout loadingCartGif;
    private TextView tvContinue;
    private Boolean valid = true;
    private LoginPreferencesManager loginPreferencesManager;
    private TextInputEditText edtFirstName,edtLastName,edtPhoneNo,edtEmail;

    private Retrofit2Client retrofit2Client;
    private BookingService bookingService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_guest_house_booking);

        //padding status bar and bottom navigation bar
        View RootLayout = findViewById(R.id.cordinatorLyt);
        View toolbar = findViewById(R.id.htab_appbar);
        paddingStatusBar(toolbar);
        paddingBottomNavigationBar(RootLayout);

        retrofit2Client=new Retrofit2Client(getApplicationContext());
        bookingService=retrofit2Client.createService(BookingService.class);

        // initialisation
        tv_booking_info = findViewById(R.id.toolbar_custom_sub_title);
        txtStartDate = findViewById(R.id.txtStartDate);
        txtEndDate = findViewById(R.id.txtEndDate);
        txtNight = findViewById(R.id.txtNight);


        tvPropertyName = findViewById(R.id.tvPropertyName);
        tvPropertyAddress = findViewById(R.id.tvPropertyAddress);
        propertyImageView = findViewById(R.id.propertyImageView);

        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName= findViewById(R.id.edtLastName);
        edtPhoneNo= findViewById(R.id.edtPhoneNo);
        edtEmail= findViewById(R.id.edtEmail);

        unStrikedPrice = findViewById(R.id.unStrikedPrice);
        txtPerNight = findViewById(R.id.txtPerNight);
        btn_next = findViewById(R.id.btn_next);
        loadingCartGif = findViewById(R.id.loadingCartGif);
        tvContinue = findViewById(R.id.continueBtn);
        loginPreferencesManager = new LoginPreferencesManager(getApplicationContext());


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
            property_price= intent.getStringExtra("property_price");
            price_currency= intent.getStringExtra("price_currency");
            nb_adultes = intent.getStringExtra("nb_adultes");
            nb_enfants = intent.getStringExtra("nb_enfants");


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
            tvPropertyName.setText(property_name);
            tvPropertyAddress.setText(property_adress);

            //calculate total amount
            long nbOfNight=getDaysBetween(checkinDate,checkoutDate);
            Double price=Double.parseDouble(property_price);
            Double total=price*nbOfNight;
            unStrikedPrice.setText(String.valueOf(total));
            txtPerNight.setText(price_currency);

            //images
            String baseUrlProperty = Constant.BASE_URL + "api/v1/property-image/";
            String fullImageUrlProperty = baseUrlProperty+ property_first_image;

            Glide.with(this)
                    .load(fullImageUrlProperty)
                    .placeholder(R.drawable.hotel_place_holder)
                    .into(propertyImageView);

            //user info
            User user = loginPreferencesManager.getUser();
            if (user != null) {

                edtFirstName.setText(user.getFirst_name());
                edtLastName.setText(user.getLast_name());
                edtPhoneNo.setText(user.getPhone_number());
                edtEmail.setText(user.getEmail());

            }



        }

        btn_next.setOnClickListener(view -> {

            if (!loginPreferencesManager.isLoggedIn()) {

                Intent intentLogin = new Intent(this, LoginActivity.class);
                loginActivityLauncher.launch(intentLogin); // Use ActivityResultLauncher
                //return false; // Prevent navigation to AccountFragment
            }
            else{

                loadingCartGif.setVisibility(View.VISIBLE);
                btn_next.setEnabled(false);
                tvContinue.setText("");

                User user = loginPreferencesManager.getUser();
                if (user != null) {

                    String user_id=String.valueOf(user.getId());
                    String pricing_type="daily";
                    String duration=String.valueOf(getDaysBetween(checkinDate,checkoutDate));
                    String total_price=unStrikedPrice.getText().toString();
                    String booking_type="guest_house";
                    String first_name=edtFirstName.getText().toString();
                    String last_name=edtLastName.getText().toString();
                    String phone=edtPhoneNo.getText().toString();
                    String email=edtEmail.getText().toString();

                    saveBooking(
                            Integer.valueOf(user_id),Integer.valueOf(property_id),
                            checkinDate,checkoutDate,Double.valueOf(property_price),
                            pricing_type,duration,
                            Double.valueOf(total_price),price_currency,
                            booking_type,first_name,last_name,phone,email,1,0,1
                    );



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

    private final ActivityResultLauncher<Intent> loginActivityLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // User logged in, proceed with original action
                    continueAfterLogin();
                }
            });

    private void continueAfterLogin() {
        Toast.makeText(this, "Continuing with Activity A data intact!", Toast.LENGTH_SHORT).show();
        // Your logic to proceed after login
    }



    public void saveBooking(int user_id,
                            int property_id, String checkinDate,
                            String checkoutDate, double property_price,
                            String pricing_type, String duration,
                            double total_price, String price_currency,
                            String booking_type, String first_name,
                            String last_name,
                            String phone, String email,int nb_adultes, int nb_enfants, int room_id) {

        Booking booking = new Booking (
                user_id, property_id,
                checkinDate, checkoutDate,
                property_price, pricing_type,
                duration, total_price,
                price_currency, booking_type,
                first_name, last_name, phone, email,nb_adultes,nb_enfants,room_id);

        // Send the POST request
        Call<Booking> call = bookingService.saveBooking(booking);
        call.enqueue(new Callback<Booking>() {
            @Override
            public void onResponse(Call<Booking> call, Response<Booking> response) {
                if (response.isSuccessful()) {

                    loadingCartGif.setVisibility(View.GONE);
                    btn_next.setEnabled(true);
                    tvContinue.setText("Continuer");

                    SuccessDialogFragment successDialogFragment = new SuccessDialogFragment(() -> {

                        Intent intent = new Intent(ConfirmGuestHouseBookingActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    });
                    Bundle args = new Bundle();
                    args.putString("success_message", "Reservation fait avec success");
                    successDialogFragment.setArguments(args);
                    successDialogFragment.show(getSupportFragmentManager(), "SuccessDialog");

                } else {

                    loadingCartGif.setVisibility(View.GONE);
                    btn_next.setEnabled(true);
                    tvContinue.setText("Continuer");

                    handleValidationError(response);
                }
            }

            @Override
            public void onFailure(Call<Booking> call, Throwable t) {
                Toast.makeText(ConfirmGuestHouseBookingActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("BookingError", t.getMessage());

                loadingCartGif.setVisibility(View.GONE);
                btn_next.setEnabled(true);
                tvContinue.setText("Continuer");
            }
        });

    }

    private void handleValidationError(Response<?> response) {
        try {
            String errorBody = response.errorBody().string(); // Raw error response
            Gson gson = new Gson();
            ValidationErrorResponse errorResponse = gson.fromJson(errorBody, ValidationErrorResponse.class);

            if (errorResponse != null && errorResponse.getData() != null) {
                // Build a dynamic error message
                StringBuilder errorMessages = new StringBuilder("Validation Errors:\n");

                for (Map.Entry<String, List<String>> entry : errorResponse.getData().entrySet()) {
                    String field = entry.getKey();
                    List<String> messages = entry.getValue();

                    errorMessages.append("- ").append(field).append(": ");
                    for (String message : messages) {
                        errorMessages.append(message).append(" ");
                    }
                    errorMessages.append("\n");
                }

                // Show the AlertDialog
                showErrorDialog(errorMessages.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("An unexpected error occurred.");
        }
    }
    private void showErrorDialog(String errorMessages) {
        new AlertDialog.Builder(this)
                .setTitle("Validation Errors")
                .setMessage(errorMessages)
                .setPositiveButton("OK", null)
                .show();
    }


}