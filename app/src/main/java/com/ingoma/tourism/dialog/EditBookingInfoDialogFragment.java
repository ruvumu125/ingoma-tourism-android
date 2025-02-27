package com.ingoma.tourism.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.ingoma.tourism.HotelDatePickerActivity;
import com.ingoma.tourism.LocationSearchActivity;
import com.ingoma.tourism.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class EditBookingInfoDialogFragment extends BottomSheetDialogFragment implements GuestSelectionDialogFragment.CallBackListener {

    private LinearLayoutCompat layout_check_in_date,layout_check_out_date,layout_guest,layout_destination;
    private AppCompatTextView tv_from_city,checkinDate,checkoutDate,tv_no_of_guest;
    private MaterialTextView checkinDay,checkoutDay;
    private String property_type,city_or_property_edit,checkinDateEdit,checkoutDateEdit,nb_adultes_edit,nb_enfants_edit;
    private MaterialButton btnDone;
    private LinearLayoutCompat Llc_guest;

    private CallBackListener callBackListener;

    public interface CallBackListener {
        void onModifyButtonClicked(String city_or_property_response,String checkinDate_response,String checkoutDate_response,String checkinDateFrenchFormat_response,String checkoutDateFrenchFormat_response,int adultesNumber_response, int childrenNumber_response);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (callBackListener == null) { // If not set manually
            try {
                callBackListener = (CallBackListener) context;
            } catch (ClassCastException e) {
                throw new ClassCastException(context.toString() + " must implement CallBackListener");
            }
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View root=inflater.inflate(R.layout.fragment_edit_booking_info_dialog, container, false);

        // Find TextViews
        tv_from_city=root.findViewById(R.id.tv_from_city_edit);
        checkinDay = root.findViewById(R.id.tv_check_in_day_edit);
        checkinDate = root.findViewById(R.id.tv_check_in_date_edit);
        checkoutDay = root.findViewById(R.id.tv_checkout_day_edit);
        checkoutDate = root.findViewById(R.id.tv_checkout_date_edit);
        tv_no_of_guest = root.findViewById(R.id.tv_no_of_guest_edit);
        layout_check_in_date= root.findViewById(R.id.layout_check_in_date_edit);
        layout_check_out_date=root.findViewById(R.id.layout_check_out_date_edit);
        layout_guest=root.findViewById(R.id.layout_guest_edit);
        layout_destination=root.findViewById(R.id.layout_destination_edit);
        Llc_guest=root.findViewById(R.id.Llc_guest);
        btnDone=root.findViewById(R.id.btnDoneEdit);

        // Retrieve data from the arguments
        Bundle arguments = getArguments();
        if (arguments != null) {

            city_or_property_edit= arguments.getString("city_or_property");
            checkinDateEdit= arguments.getString("checkinDate");
            checkoutDateEdit= arguments.getString("checkoutDate");
            nb_adultes_edit= arguments.getString("nb_adultes");
            nb_enfants_edit= arguments.getString("nb_enfants");
            property_type = arguments.getString("property_type");

            if (property_type.equals("hotel")){
                Llc_guest.setVisibility(View.VISIBLE);
            }
            else {
                Llc_guest.setVisibility(View.GONE);
            }

            displayDefaultDates(checkinDateEdit,checkoutDateEdit);
            sumOfGuest(Integer.valueOf(nb_adultes_edit), Integer.valueOf(nb_enfants_edit));
            tv_from_city.setText(city_or_property_edit);
        }

        layout_check_in_date.setOnClickListener(v ->{
            openHotelDatePickerActivity();
        });
        layout_check_out_date.setOnClickListener( v ->{
            openHotelDatePickerActivity();
        });

        layout_guest.setOnClickListener(v -> {
            GuestSelectionDialogFragment guestSelectionDialogFragment = new GuestSelectionDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("nbAdultes", Integer.valueOf(nb_adultes_edit));
            bundle.putInt("nbChildren", Integer.valueOf(nb_enfants_edit));
            guestSelectionDialogFragment.setArguments(bundle);

            // Set the callback listener
            guestSelectionDialogFragment.setCallBackListener(this);

            guestSelectionDialogFragment.show(getActivity().getSupportFragmentManager(), "GuestSelectionBottomSheetDialog");
        });


        layout_destination.setOnClickListener(view -> {
            openLocationSearchActivity();
        });

        btnDone.setOnClickListener(v ->{

            if (callBackListener != null) {
                callBackListener.onModifyButtonClicked(city_or_property_edit, checkinDateEdit,checkoutDateEdit,checkinDate.getText().toString(),checkoutDate.getText().toString(),Integer.valueOf(nb_adultes_edit),Integer.valueOf(nb_enfants_edit));
                dismiss(); // Close the dialog
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

                        bottomSheet.setBackgroundResource(R.drawable.bottomsheet_round_bg);
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
                bottomSheet.setBackgroundResource(R.drawable.bottomsheet_round_bg);

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

    private void sumOfGuest(int adultesNumber, int childrenNumber) {

        int sum=adultesNumber+childrenNumber;
        if (sum>1){
            tv_no_of_guest.setText(String.valueOf(sum)+" voyageurs");
        }
        else{
            tv_no_of_guest.setText(String.valueOf(sum)+" voyageur");
        }
    }

    private final ActivityResultLauncher<Intent> selectDatesActivityResultLauncher =
    registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
                checkinDateEdit = data.getStringExtra("checkInDateResponse");
                checkoutDateEdit = data.getStringExtra("checkOutDateResponse");

                displayDefaultDates(checkinDateEdit,checkoutDateEdit);
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
                city_or_property_edit=cityName;

            }
        }
    });

    public void openHotelDatePickerActivity() {
        Intent intent = new Intent(getContext(), HotelDatePickerActivity.class);
        intent.putExtra("checkinDate", checkinDateEdit);
        intent.putExtra("checkoutDate", checkoutDateEdit);
        selectDatesActivityResultLauncher.launch(intent);
    }

    public void openLocationSearchActivity() {
        Intent intent = new Intent(getContext(), LocationSearchActivity.class);
        selectLocationActivityResultLauncher.launch(intent);
    }

    @Override
    public void onGuestSelected(int adultesNumber, int childrenNumber) {

        nb_adultes_edit=String.valueOf(adultesNumber);
        nb_enfants_edit=String.valueOf(childrenNumber);
        int sum=adultesNumber+childrenNumber;
        if (sum>1){
            tv_no_of_guest.setText(String.valueOf(sum)+" voyageurs");
        }
        else{
            tv_no_of_guest.setText(String.valueOf(sum)+" voyageur");
        }
    }
}