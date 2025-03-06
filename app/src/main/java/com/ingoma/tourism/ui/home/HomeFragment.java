package com.ingoma.tourism.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textview.MaterialTextView;
import com.ingoma.tourism.HotelDatePickerActivity;
import com.ingoma.tourism.HotelSearchActivity;
import com.ingoma.tourism.LocationSearchActivity;
import com.ingoma.tourism.PropertiesListActivity;
import com.ingoma.tourism.R;
import com.ingoma.tourism.dialog.GuestSelectionDialogFragment;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements GuestSelectionDialogFragment.CallBackListener{

    private LinearLayoutCompat Llc_guest;
    private ChipGroup chipGroup;

    private LinearLayoutCompat layout_check_in_date,layout_check_out_date,layout_guest,layout_destination;

    private AppCompatTextView tv_from_city,checkinDate,checkoutDate,tv_no_of_guest;
    private MaterialTextView checkinDay,checkoutDay;
    private String from_city="";
    private String property_type="hotel";

    private String checkinStr,checkoutStr;
    private int nbAdultes=1;
    private int nbChildren=0;
    private MaterialButton btnDone;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

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
                tv_from_city.setText(cityName);
                from_city=cityName;

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
        intent.putExtra("nb_adultes", String.valueOf(nbAdultes));
        intent.putExtra("nb_enfants",String.valueOf(nbChildren));
        intent.putExtra("property_type",property_type);

        startActivity(intent);
    }

    public void openLocationSearchActivity() {
        Intent intent = new Intent(getContext(), LocationSearchActivity.class);
        intent.putExtra("property_type",property_type);
        selectLocationActivityResultLauncher.launch(intent);
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
}