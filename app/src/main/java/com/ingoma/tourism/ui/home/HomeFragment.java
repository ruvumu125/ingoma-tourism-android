package com.ingoma.tourism.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textview.MaterialTextView;
import com.ingoma.tourism.HotelDatePickerActivity;
import com.ingoma.tourism.LocationSearchActivity;
import com.ingoma.tourism.PropertiesDetailsActivity;
import com.ingoma.tourism.PropertiesListActivity;
import com.ingoma.tourism.R;
import com.ingoma.tourism.adapter.HomeMainSliderAdapter;
import com.ingoma.tourism.adapter.SliderPropertyDetailsAdapter;
import com.ingoma.tourism.api.AdvertisementApiService;
import com.ingoma.tourism.api.Retrofit2Client;
import com.ingoma.tourism.dialog.GuestSelectionDialogFragment;
import com.ingoma.tourism.model.Advertisement;
import com.ingoma.tourism.model.AdvertisementResponse;
import com.ingoma.tourism.model.User;
import com.ingoma.tourism.utils.LoginPreferencesManager;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements GuestSelectionDialogFragment.CallBackListener{

    private LinearLayoutCompat Llc_guest;
    private ChipGroup chipGroup;

    private LinearLayoutCompat layout_check_in_date,layout_check_out_date,layout_guest,layout_destination;

    private AppCompatTextView tv_from_city,checkinDate,checkoutDate,tv_no_of_guest;
    private MaterialTextView checkinDay,checkoutDay;
    private String from_city="";
    private String type="";
    private String property_type="hotel";

    private String checkinStr,checkoutStr;
    private int nbAdultes=1;
    private int nbChildren=0;
    private MaterialButton btnDone;
    private TextView tv_hello;
    private LoginPreferencesManager loginPreferencesManager;

    private SliderView sliderView;
    private HomeMainSliderAdapter sliderAdapter;
    private List<Advertisement> imageUrls = new ArrayList<Advertisement>();
    private Retrofit2Client retrofit2Client;
    private AdvertisementApiService advertisementApiService;
    private LinearLayoutCompat deals_layout;


    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        loginPreferencesManager = new LoginPreferencesManager(getContext());
        retrofit2Client=new Retrofit2Client(getContext());
        advertisementApiService = retrofit2Client.createService(AdvertisementApiService.class);

        sliderView = root.findViewById(R.id.imageSliderAdvertisement);
        sliderAdapter=new HomeMainSliderAdapter(getContext());
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setIndicatorSelectedColor(getResources().getColor(R.color.primary));
        sliderView.setIndicatorUnselectedColor(Color.WHITE);
        sliderView.setScrollTimeInSec(6);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();


        Llc_guest = root.findViewById(R.id.Llc_guest);
        chipGroup = root.findViewById(R.id.trip_chip_group);
        tv_from_city = root.findViewById(R.id.tv_from_city);
        checkinDay = root.findViewById(R.id.tv_check_in_day);
        checkinDate = root.findViewById(R.id.tv_check_in_date);
        checkoutDay = root.findViewById(R.id.tv_checkout_day);
        checkoutDate = root.findViewById(R.id.tv_checkout_date);
        tv_no_of_guest = root.findViewById(R.id.tv_no_of_guest);
        layout_check_in_date = root.findViewById(R.id.layout_check_in_date);
        layout_check_out_date = root.findViewById(R.id.layout_check_out_date);
        layout_guest = root.findViewById(R.id.layout_guest);
        layout_destination = root.findViewById(R.id.layout_destination);
        btnDone = root.findViewById(R.id.btnDone);
        tv_hello= root.findViewById(R.id.tv_hello);
        deals_layout=root.findViewById(R.id.deals_layout);

        fetchAdvertisements(deals_layout);

        if (loginPreferencesManager.isLoggedIn()) {

            User user = loginPreferencesManager.getUser();
            if (user != null) {

                tv_hello.setText("Bonjour "+user.getLast_name());
            }
        }
        else{
            tv_hello.setText("Bonjour voyageur");
        }

        // Define default dates
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 2); // Move to the day after tomorrow
        checkinStr = sdf.format(calendar.getTime());

        calendar.add(Calendar.DAY_OF_YEAR, 4); // Move to 2 days after tomorrow
        checkoutStr = sdf.format(calendar.getTime());
        displayDefaultDates(checkinStr,checkoutStr);

        chipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {

            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {

                if (!checkedIds.isEmpty()) { // Ensure a chip is selected
                    int checkedId = checkedIds.get(0); // Get the first selected chip

                    if (checkedId == R.id.cOneway) {
                        Llc_guest.setVisibility(View.VISIBLE);
                        property_type="hotel";
                    } else if (checkedId == R.id.cRoundWay) {
                        Llc_guest.setVisibility(View.GONE);
                        property_type="guesthouse";
                    }
                }
            }
        });

        layout_check_in_date.setOnClickListener(v ->{
            openHotelDatePickerActivity();
        });
        layout_check_out_date.setOnClickListener( v ->{
            openHotelDatePickerActivity();
        });

        layout_guest.setOnClickListener( v-> {

            GuestSelectionDialogFragment guestSelectionDialogFragment = new GuestSelectionDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("nbAdultes", nbAdultes);
            bundle.putInt("nbChildren", nbChildren);
            guestSelectionDialogFragment.setArguments(bundle);
            guestSelectionDialogFragment.setCallBackListener(this); // Passe le Listener ici


            guestSelectionDialogFragment.show(getActivity().getSupportFragmentManager(), "GuestSelectionBottomSheetDialog");
        });

        layout_destination.setOnClickListener(view -> {
            openLocationSearchActivity();
        });

        btnDone.setOnClickListener(v ->{

            if (from_city.equals("")){

                Toast toast = Toast.makeText(getContext(), "Veuillez sélectionner une destination", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            openPropertyListActivity();

        });



        return root;
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

    private final ActivityResultLauncher<Intent> selectDatesActivityResultLauncher =
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
    private final ActivityResultLauncher<Intent> selectLocationActivityResultLauncher =
    registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {

                String cityName = data.getStringExtra("selected_city");
                String  type_search = data.getStringExtra("type");
                tv_from_city.setText(cityName);
                from_city=cityName;
                type=type_search;

            }
        }
    });
    public void openHotelDatePickerActivity() {
        Intent intent = new Intent(getContext(), HotelDatePickerActivity.class);
        intent.putExtra("checkinDate", checkinStr);
        intent.putExtra("checkoutDate", checkoutStr);
        selectDatesActivityResultLauncher.launch(intent);
    }

    public void openPropertyListActivity() {
        Intent intent = new Intent(getContext(), PropertiesListActivity.class);
        intent.putExtra("checkinDate", checkinStr);
        intent.putExtra("checkoutDate", checkoutStr);
        intent.putExtra("checkinDateFrench", checkinDate.getText().toString());
        intent.putExtra("checkoutDateFrench", checkoutDate.getText().toString());
        intent.putExtra("city_or_property", from_city);
        intent.putExtra("type", type);
        intent.putExtra("nb_adultes", String.valueOf(nbAdultes));
        intent.putExtra("nb_enfants",String.valueOf(nbChildren));
        intent.putExtra("property_type",property_type);
        intent.putExtra("tarification_type",getTarificationType(checkinStr,checkoutStr));

        startActivity(intent);
    }

    public void openLocationSearchActivity() {
        Intent intent = new Intent(getContext(), LocationSearchActivity.class);
        intent.putExtra("property_type",property_type);
        selectLocationActivityResultLauncher.launch(intent);
    }

    private void fetchAdvertisements(LinearLayoutCompat deals_layout) {

        Call<AdvertisementResponse> call = advertisementApiService.getAdvertisements();

        call.enqueue(new Callback<AdvertisementResponse>() {
            @Override
            public void onResponse(Call<AdvertisementResponse> call, Response<AdvertisementResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Advertisement> ads = response.body().getData();

                    if (ads.isEmpty()){
                        deals_layout.setVisibility(View.GONE);
                    }
                    else{
                        deals_layout.setVisibility(View.VISIBLE);

                        imageUrls.addAll(ads);
                        sliderAdapter.renewItems(imageUrls);
                    }


                } else {
                    Toast.makeText(getContext(), "Failed to retrieve images", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AdvertisementResponse> call, Throwable t) {

                Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onGuestSelected(int adultesNumber, int childrenNumber) {

        nbAdultes=adultesNumber;
        nbChildren=childrenNumber;
        int sum=adultesNumber+childrenNumber;
        if (sum>1){
            tv_no_of_guest.setText(String.valueOf(sum)+" voyageurs");
        }
        else{
            tv_no_of_guest.setText(String.valueOf(sum)+" voyageur");
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