package com.ingoma.tourism.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ingoma.tourism.R;
import com.ingoma.tourism.adapter.RoomImagesAdapter;
import com.ingoma.tourism.model.RoomImage;

import java.util.ArrayList;
import java.util.List;


public class RoomPhotosDialogFragment extends BottomSheetDialogFragment {

    private List<RoomImage> imagesList;
    private String roomName;
    private TextView toolbar_custom_title;

    public static RoomPhotosDialogFragment newInstance(List<RoomImage> imagesList, String roomName) {
        RoomPhotosDialogFragment fragment = new RoomPhotosDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("images_list", new ArrayList<>(imagesList)); // Ensure it's Serializable
        args.putString("room_name", roomName);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View root=inflater.inflate(R.layout.fragment_room_photos_dialog, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.rv_room_pictures);
        toolbar_custom_title=root.findViewById(R.id.toolbar_custom_title);
        toolbar_custom_title.setText(roomName);
        RoomImagesAdapter adapter = new RoomImagesAdapter(getContext(),imagesList,roomName);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(adapter);

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

        if (getArguments() != null) {
            imagesList = (List<RoomImage>) getArguments().getSerializable("images_list");
            roomName = getArguments().getString("room_name");
        }
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
}