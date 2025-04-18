package com.ingoma.tourism.dialog;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ingoma.tourism.PropertiesDetailsActivity;
import com.ingoma.tourism.R;
import com.ingoma.tourism.adapter.PropertyAmenitiesAdapter;
import com.ingoma.tourism.adapter.RoomAmenitiesAdapter;
import com.ingoma.tourism.adapter.SliderPropertyDetailsAdapter;
import com.ingoma.tourism.adapter.SliderRoomDetailsAdapter;
import com.ingoma.tourism.model.PropertyAmenity;
import com.ingoma.tourism.model.PropertyImage;
import com.ingoma.tourism.model.RoomAmenity;
import com.ingoma.tourism.model.RoomHotel;
import com.ingoma.tourism.model.RoomImage;
import com.smarteist.autoimageslider.SliderView;

import java.util.List;


public class RoomDetailsDialogFragment extends BottomSheetDialogFragment {

    private RoomHotel room;
    private String property_name;
    private TextView tv_property_name,tvAmHeadingRoom,tv_room_guest_count,tv_room_bed_type,tv_room_surface;
    private SliderView sliderView;
    private RecyclerView rvAmenities;
    private AppCompatTextView tv_room_description;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View root=inflater.inflate(R.layout.fragment_room_details_dialog, container, false);
        sliderView = root.findViewById(R.id.view_pager_room);
        tv_property_name = root.findViewById(R.id.tv_property_name);
        tvAmHeadingRoom = root.findViewById(R.id.tvAmHeadingRoom);
        tv_room_description=root.findViewById(R.id.tv_room_description);
        tv_room_guest_count=root.findViewById(R.id.tv_room_guest_count);
        tv_room_bed_type=root.findViewById(R.id.tv_room_bed_type);
        tv_room_surface=root.findViewById(R.id.tv_room_surface);
        rvAmenities=root.findViewById(R.id.rvRoomAm);
        rvAmenities.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getArguments() != null) {
            room = getArguments().getParcelable("room");
            property_name = getArguments().getString("property_name");
            getRoomDetails(room,property_name);
        }

        return root;
    }

    private void getRoomDetails(RoomHotel roomHotel,String property_name){

        tvAmHeadingRoom.setText(roomHotel.getTypeName());
        tv_room_description.setText(roomHotel.getDescription());
        tv_property_name.setText(property_name);

        //guest
        String guest="";
        if (Integer.valueOf(roomHotel.getMaxGuests())>1){
            guest=roomHotel.getMaxGuests()+" visiteurs";
        }
        else{
            guest=roomHotel.getMaxGuests()+" visiteur";
        }
        tv_room_guest_count.setText(guest);
        tv_room_bed_type.setText(roomHotel.getBedType());
        tv_room_surface.setText(room.getRoomSize() + " m²");

        // Load RoomImage Slider
        List<RoomImage> roomImages=roomHotel.getImages();
        sliderView.setSliderAdapter(new SliderRoomDetailsAdapter(getContext(), roomImages));
        sliderView.startAutoCycle();

        // amenities
        List<RoomAmenity> roomAmenities = roomHotel.getAmenities();
        rvAmenities.setAdapter(new RoomAmenitiesAdapter(roomAmenities));

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