package com.ingoma.tourism;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowInsets;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import com.ingoma.tourism.api.BookingService;
import com.ingoma.tourism.api.LoginService;
import com.ingoma.tourism.api.Retrofit2Client;
import com.ingoma.tourism.api.UserApiService;
import com.ingoma.tourism.constant.Constant;
import com.ingoma.tourism.dialog.SuccessDialogFragment;
import com.ingoma.tourism.model.Booking;
import com.ingoma.tourism.model.HotelModel;
import com.ingoma.tourism.model.User;
import com.ingoma.tourism.model.ValidationErrorResponse;
import com.ingoma.tourism.utils.LoginPreferencesManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmHotelBookingActivity extends AppCompatActivity {

    private String tarification_type,property_price,price_currency,plan_id,plan_name,plan_description,room_id,room_name,room_size,room_bed_type,room_main_image,property_id,property_name,property_adress,property_first_image,property_type,checkinDate,checkoutDate,checkinDateFrench,checkoutDateFrench,city_or_property,nb_adultes,nb_enfants;
    private TextView tv_booking_info,txtStartDate,txtEndDate,txtNight;
    private TextView tv_property_type,tvPropertyName,tvPropertyAddress,tvRooName,tvPlanName,tvPlanDescription;
    private AppCompatImageView propertyImageView,roomImageView;
    private TextView unStrikedPrice,txtPerNight;

    private ConstraintLayout btn_next;
    private ConstraintLayout loadingCartGif;
    private TextView tvContinue;
    private Boolean valid = true;
    private LoginPreferencesManager loginPreferencesManager;
    private TextInputEditText edtFirstName,edtLastName;
    private CountryCodePicker ccp;
    private AppCompatEditText edtPhoneNo;

    private Retrofit2Client retrofit2Client;
    private BookingService bookingService;
    private UserApiService userApiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_hotel_booking);

        //padding status bar and bottom navigation bar
        View RootLayout = findViewById(R.id.cordinatorLyt);
        View toolbar = findViewById(R.id.htab_appbar);
        paddingStatusBar(toolbar);
        paddingBottomNavigationBar(RootLayout);

        retrofit2Client=new Retrofit2Client(getApplicationContext());
        bookingService=retrofit2Client.createService(BookingService.class);
        userApiService=retrofit2Client.createService(UserApiService.class);

        // initialisation
        tv_booking_info = findViewById(R.id.toolbar_custom_sub_title);
        txtStartDate = findViewById(R.id.txtStartDate);
        txtEndDate = findViewById(R.id.txtEndDate);
        txtNight = findViewById(R.id.txtNight);

        tv_property_type = findViewById(R.id.tv_property_type);
        tvPropertyName = findViewById(R.id.tvPropertyName);
        tvPropertyAddress = findViewById(R.id.tvPropertyAddress);
        tvRooName = findViewById(R.id.tvRooName);
        tvPlanName= findViewById(R.id.tvPlanName);
        tvPlanDescription= findViewById(R.id.tvPlanDescription);
        propertyImageView = findViewById(R.id.propertyImageView);
        roomImageView = findViewById(R.id.roomImageView);

        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName= findViewById(R.id.edtLastName);
        edtPhoneNo= findViewById(R.id.edtPhoneNo);

        unStrikedPrice = findViewById(R.id.unStrikedPrice);
        txtPerNight = findViewById(R.id.txtPerNight);
        btn_next = findViewById(R.id.btn_next);
        loadingCartGif = findViewById(R.id.loadingCartGif);
        tvContinue = findViewById(R.id.continueBtn);
        loginPreferencesManager = new LoginPreferencesManager(getApplicationContext());

        ccp = findViewById(R.id.ccp);
        // Map numeric country code to name code
        String countryCode = "+257";  // Country code for the USA
        String countryNameCode = getCountryNameCode(countryCode);

        //Set the default country using the country name code
        if (countryNameCode != null) {
            ccp.setCountryForNameCode(countryNameCode);
        }


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

            room_id= intent.getStringExtra("room_id");
            room_name= intent.getStringExtra("room_name");
            room_size= intent.getStringExtra("room_size");
            room_bed_type = intent.getStringExtra("room_bed_type");
            room_main_image= intent.getStringExtra("room_main_image");

            plan_id=intent.getStringExtra("plan_id");
            plan_name= intent.getStringExtra("plan_name");
            plan_description= intent.getStringExtra("plan_description");

            property_price= intent.getStringExtra("property_price");
            price_currency= intent.getStringExtra("price_currency");
            tarification_type=intent.getStringExtra("tarification_type");


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
            if (plan_description != null && !plan_description.equals("null")){
                tvPlanDescription.setText(plan_description);
            }

            //calculate total amount
            long nbOfNight=getDaysBetween(checkinDate,checkoutDate);
            Double price=Double.parseDouble(property_price);
            Double total=price*nbOfNight;
            unStrikedPrice.setText(String.valueOf(total));
            txtPerNight.setText(price_currency);

            //images
            String baseUrlProperty = Constant.BASE_URL + "api/v1/property-image/";
            String fullImageUrlProperty = baseUrlProperty+ property_first_image;

            String baseUrlRoom = Constant.BASE_URL + "api/v1/room-image/";
            String fullImageUrlRoom = baseUrlRoom+ room_main_image;

            Glide.with(this)
                    .load(fullImageUrlProperty)
                    .placeholder(R.drawable.hotel_place_holder)
                    .into(propertyImageView);

            Glide.with(this)
                    .load(fullImageUrlRoom)
                    .placeholder(R.drawable.hotel_place_holder)
                    .into(roomImageView);

            //user info
            User user = loginPreferencesManager.getUser();
            if (user != null) {
                edtFirstName.setText(user.getFirst_name());
                edtLastName.setText(user.getLast_name());
                edtPhoneNo.setText(user.getPhone_number());
                //edtEmail.setText(user.getEmail());

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

                fetchCurrentUser(new UserCallback() {
                    @Override
                    public void onUserReceived(User user) {

                        if (user.getPreferred_notification_channel()== null || user.getPreferred_notification_channel().equals("") ){

                            loadingCartGif.setVisibility(View.GONE);
                            btn_next.setEnabled(true);
                            tvContinue.setText("Réserver");
                            tvContinue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

                            Intent intent = new Intent(ConfirmHotelBookingActivity.this, NotificationChannelActivity.class);
                            intent.putExtra("user_email", user.getEmail());
                            startActivity(intent);
                        }
                        else{

                            if (user != null) {

                                String user_id=String.valueOf(user.getId());
                                String pricing_type="daily";
                                String duration=String.valueOf(getDaysBetween(checkinDate,checkoutDate));
                                String total_price=unStrikedPrice.getText().toString();
                                String booking_type="hotel";
                                String first_name=edtFirstName.getText().toString();
                                String last_name=edtLastName.getText().toString();
                                String phone=ccp.getSelectedCountryCodeWithPlus()+edtPhoneNo.getText().toString();

                                String devise_prix="";
                                if (price_currency.equals("BIF")){
                                    devise_prix="bif";
                                }
                                else{
                                    devise_prix="dollar";
                                }

                                saveBooking(
                                        Integer.valueOf(user_id),Integer.valueOf(property_id),checkinDate,checkoutDate,Double.valueOf(property_price),
                                        pricing_type,duration,Double.valueOf(total_price),devise_prix,booking_type,
                                        first_name,last_name,phone,Integer.valueOf(nb_adultes),Integer.valueOf(nb_enfants),Integer.valueOf(room_id),Integer.valueOf(plan_id)
                                );



                            }
                        }

                    }

                    @Override
                    public void onError(String errorMessage) {

                        loadingCartGif.setVisibility(View.GONE);
                        btn_next.setEnabled(true);
                        tvContinue.setText("Réserver");
                        tvContinue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

                        Toast.makeText(getApplicationContext(),errorMessage,Toast.LENGTH_LONG);
                    }
                });

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

    public interface UserCallback {
        void onUserReceived(User user);
        void onError(String errorMessage);
    }


    public void fetchCurrentUser(UserCallback callback) {

        Call<User> call = userApiService.getCurrentUser();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onUserReceived(response.body());
                } else {
                    callback.onError("Error code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    private String getCountryNameCode(String countryCode) {
        switch (countryCode) {
            case "+1":
                return "US";  // United States
            case "+7":
                return "RU";  // Russia
            case "+20":
                return "EG";  // Egypt
            case "+30":
                return "GR";  // Greece
            case "+31":
                return "NL";  // Netherlands
            case "+32":
                return "BE";  // Belgium
            case "+33":
                return "FR";  // France
            case "+34":
                return "ES";  // Spain
            case "+39":
                return "IT";  // Italy
            case "+40":
                return "RO";  // Romania
            case "+41":
                return "CH";  // Switzerland
            case "+42":
                return "AT";  // Austria
            case "+43":
                return "PL";  // Poland
            case "+44":
                return "GB";  // United Kingdom
            case "+45":
                return "DK";  // Denmark
            case "+46":
                return "SE";  // Sweden
            case "+47":
                return "NO";  // Norway
            case "+48":
                return "PL";  // Poland
            case "+49":
                return "DE";  // Germany
            case "+52":
                return "MX";  // Mexico
            case "+53":
                return "CU";  // Cuba
            case "+54":
                return "AR";  // Argentina
            case "+55":
                return "BR";  // Brazil
            case "+56":
                return "CL";  // Chile
            case "+57":
                return "CO";  // Colombia
            case "+58":
                return "VE";  // Venezuela
            case "+60":
                return "MY";  // Malaysia
            case "+61":
                return "AU";  // Australia
            case "+62":
                return "ID";  // Indonesia
            case "+63":
                return "PH";  // Philippines
            case "+64":
                return "NZ";  // New Zealand
            case "+65":
                return "SG";  // Singapore
            case "+66":
                return "TH";  // Thailand
            case "+81":
                return "JP";  // Japan
            case "+82":
                return "KR";  // South Korea
            case "+84":
                return "VN";  // Vietnam
            case "+86":
                return "CN";  // China
            case "+90":
                return "TR";  // Turkey
            case "+91":
                return "IN";  // India
            case "+92":
                return "PK";  // Pakistan
            case "+93":
                return "AF";  // Afghanistan
            case "+94":
                return "LK";  // Sri Lanka
            case "+95":
                return "MM";  // Myanmar
            case "+98":
                return "IR";  // Iran
            case "+212":
                return "MA";  // Morocco
            case "+213":
                return "DZ";  // Algeria
            case "+216":
                return "TN";  // Tunisia
            case "+218":
                return "LY";  // Libya
            case "+220":
                return "GM";  // Gambia
            case "+221":
                return "SN";  // Senegal
            case "+222":
                return "MR";  // Mauritania
            case "+223":
                return "ML";  // Mali
            case "+224":
                return "GN";  // Guinea
            case "+225":
                return "CI";  // Ivory Coast
            case "+226":
                return "BF";  // Burkina Faso
            case "+227":
                return "NE";  // Niger
            case "+228":
                return "TG";  // Togo
            case "+229":
                return "BJ";  // Benin
            case "+230":
                return "MU";  // Mauritius
            case "+231":
                return "LR";  // Liberia
            case "+232":
                return "SL";  // Sierra Leone
            case "+233":
                return "GH";  // Ghana
            case "+234":
                return "NG";  // Nigeria
            case "+235":
                return "CM";  // Cameroon
            case "+236":
                return "CF";  // Central African Republic
            case "+237":
                return "TD";  // Chad
            case "+238":
                return "CV";  // Cape Verde
            case "+239":
                return "ST";  // São Tomé and Príncipe
            case "+240":
                return "GQ";  // Equatorial Guinea
            case "+241":
                return "GA";  // Gabon
            case "+242":
                return "CG";  // Republic of the Congo
            case "+243":
                return "CD";  // Democratic Republic of the Congo
            case "+244":
                return "AO";  // Angola
            case "+245":
                return "GW";  // Guinea-Bissau
            case "+246":
                return "IO";  // British Indian Ocean Territory
            case "+247":
                return "AC";  // Ascension Island
            case "+248":
                return "SC";  // Seychelles
            case "+249":
                return "SD";  // Sudan
            case "+250":
                return "SS";  // South Sudan
            case "+251":
                return "ET";  // Ethiopia
            case "+252":
                return "SO";  // Somalia
            case "+253":
                return "DJ";  // Djibouti
            case "+254":
                return "KE";  // Kenya
            case "+255":
                return "TZ";  // Tanzania
            case "+256":
                return "UG";  // Uganda
            case "+257":
                return "BI";  // Burundi
            case "+258":
                return "MZ";  // Mozambique
            case "+260":
                return "ZM";  // Zambia
            case "+261":
                return "MW";  // Malawi
            case "+262":
                return "RE";  // Réunion (France)
            case "+263":
                return "ZW";  // Zimbabwe
            case "+264":
                return "NA";  // Namibia
            case "+265":
                return "ZA";  // South Africa
            case "+266":
                return "LS";  // Lesotho
            case "+267":
                return "BW";  // Botswana
            case "+268":
                return "SZ";  // Eswatini
            case "+269":
                return "KM";  // Comoros
            case "+290":
                return "SH";  // Saint Helena
            case "+291":
                return "ER";  // Eritrea
            case "+292":
                return "DJ";  // Djibouti
            case "+350":
                return "GI";  // Gibraltar
            case "+351":
                return "PT";  // Portugal
            case "+352":
                return "LU";  // Luxembourg
            case "+353":
                return "IE";  // Ireland
            case "+354":
                return "IS";  // Iceland
            case "+355":
                return "AL";  // Albania
            case "+356":
                return "MT";  // Malta
            case "+357":
                return "CY";  // Cyprus
            case "+358":
                return "FI";  // Finland
            case "+359":
                return "BG";  // Bulgaria
            case "+370":
                return "LT";  // Lithuania
            case "+371":
                return "LV";  // Latvia
            case "+372":
                return "EE";  // Estonia
            case "+373":
                return "MD";  // Moldova
            case "+374":
                return "AM";  // Armenia
            case "+375":
                return "BY";  // Belarus
            case "+376":
                return "AD";  // Andorra
            case "+377":
                return "MC";  // Monaco
            case "+378":
                return "SM";  // San Marino
            case "+379":
                return "VA";  // Vatican City
            case "+380":
                return "UA";  // Ukraine
            case "+381":
                return "RS";  // Serbia
            case "+382":
                return "ME";  // Montenegro
            case "+383":
                return "XK";  // Kosovo
            case "+385":
                return "HR";  // Croatia
            case "+386":
                return "SI";  // Slovenia
            case "+387":
                return "BA";  // Bosnia and Herzegovina
            case "+388":
                return "MK";  // North Macedonia
            case "+389":
                return "AL";  // Albania
            default:
                return null;  // Country code not found
        }
    }

    public void saveBooking(int user_id, int property_id, String checkinDate, String checkoutDate, double property_price, String pricing_type, String duration, double total_price, String price_currency, String booking_type, String first_name, String last_name, String phone, int nb_adultes, int nb_enfants, int room_id, int room_plan_id) {

        Booking booking = new Booking (user_id, property_id, checkinDate, checkoutDate, property_price, pricing_type, duration, total_price, price_currency, booking_type, first_name, last_name, phone, nb_adultes, nb_enfants, room_id, room_plan_id);

        // Send the POST request
        Call<Booking> call = bookingService.saveBooking(booking);
        call.enqueue(new Callback<Booking>() {
            @Override
            public void onResponse(Call<Booking> call, Response<Booking> response) {
                if (response.isSuccessful()) {

                    loadingCartGif.setVisibility(View.GONE);
                    btn_next.setEnabled(true);
                    tvContinue.setText("Réserver");
                    tvContinue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

                    SuccessDialogFragment successDialogFragment = new SuccessDialogFragment(() -> {

                        Intent intent = new Intent(ConfirmHotelBookingActivity.this, MainActivity.class);
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
                    tvContinue.setText("Réserver");
                    tvContinue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

                    handleValidationError(response);
                }
            }

            @Override
            public void onFailure(Call<Booking> call, Throwable t) {
                Toast.makeText(ConfirmHotelBookingActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("BookingError", t.getMessage());

                loadingCartGif.setVisibility(View.GONE);
                btn_next.setEnabled(true);
                tvContinue.setText("Réserver");
                tvContinue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
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