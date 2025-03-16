package com.ingoma.tourism.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ingoma.tourism.R;
import com.ingoma.tourism.adapter.SortAdapter;
import com.ingoma.tourism.model.Amenity;
import com.ingoma.tourism.model.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PropertySortDialogFragment extends BottomSheetDialogFragment {

    private SortAdapter adapter;

    private Sort selectedSort;
    private List<Sort> sortList;
    private static final String ARG_SELECTED_SORT = "selected_sort";
    private OnSortSelectedListener listener;

    // Interface for communication
    public interface OnSortSelectedListener {
        void onSortSelected(Sort sort);
    }

    public static PropertySortDialogFragment newInstance(Sort selectedSort) {
        PropertySortDialogFragment fragment = new PropertySortDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SELECTED_SORT, selectedSort);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnSortSelectedListener) {
            listener = (OnSortSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnSortSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View root=inflater.inflate(R.layout.fragment_property_sort_dialog, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.rvSorts);
        AppCompatTextView btnShowSelected = root.findViewById(R.id.btnAction);

        //debut
        // Get previously selected sort option
        if (getArguments() != null) {
            selectedSort = (Sort) getArguments().getSerializable(ARG_SELECTED_SORT);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Sample sort options
        // Sample sort options
        sortList = Arrays.asList(
                new Sort("Prix le plus bas"),
                new Sort("Prix le plus haut")
        );
        // Restore selected state
        if (selectedSort != null) {
            for (Sort sort : sortList) {
                if (sort.getName().equals(selectedSort.getName())) {
                    sort.setSelected(true);
                    break;
                }
            }
        }
        adapter = new SortAdapter(sortList, (sort, position) -> selectedSort = sort);
        recyclerView.setAdapter(adapter);

        btnShowSelected.setOnClickListener(v -> {

            if (listener != null && selectedSort != null) {
                listener.onSortSelected(selectedSort);
            }
            dismiss();
        });
        //fin

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
}