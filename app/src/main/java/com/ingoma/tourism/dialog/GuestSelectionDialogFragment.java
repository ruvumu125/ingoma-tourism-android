package com.ingoma.tourism.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;

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
import com.ingoma.tourism.R;


public class GuestSelectionDialogFragment extends BottomSheetDialogFragment {

    private TextView tv_ad,tv_ch;
    private int nbAdultes=1;
    private int nbChildren=0;
    private AppCompatImageButton ib_ad_min,ib_ad_plus,ib_ch_min,ib_ch_plus;
    private MaterialButton btn_done;

    private CallBackListener callBackListener;

    public interface CallBackListener {
        void onGuestSelected(int adultesNumber, int childrenNumber);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Make sure the activity implements the listener interface
        if (context instanceof CallBackListener) {
            callBackListener = (CallBackListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement GuestSelectionListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_guest_selection_dialog, container, false);

        tv_ad=root.findViewById(R.id.tv_ad);
        tv_ch=root.findViewById(R.id.tv_ch);
        ib_ad_min=root.findViewById(R.id.ib_ad_min);
        ib_ad_plus=root.findViewById(R.id.ib_ad_plus);
        ib_ch_min=root.findViewById(R.id.ib_ch_min);
        ib_ch_plus=root.findViewById(R.id.ib_ch_plus);
        btn_done=root.findViewById(R.id.btn_done);

        // Retrieve data from the arguments
        Bundle arguments = getArguments();
        if (arguments != null) {
            nbAdultes = arguments.getInt("nbAdultes");
            nbChildren =  arguments.getInt("nbChildren");

            tv_ad.setText(String.valueOf(nbAdultes));
            tv_ch.setText(String.valueOf(nbChildren));
        }

        //increment/decrement adult
        ib_ad_plus.setOnClickListener(v -> {
            if (nbAdultes < 10) {
                nbAdultes++;
                tv_ad.setText(String.valueOf(nbAdultes));
            }
        });

        ib_ad_min.setOnClickListener(v -> {
            if (nbAdultes > 1) {
                nbAdultes--;
                tv_ad.setText(String.valueOf(nbAdultes));
            }
        });

        //increment/decrement children
        ib_ch_plus.setOnClickListener(v -> {
            if (nbChildren < 10) {
                nbChildren++;
                tv_ch.setText(String.valueOf(nbChildren));
            }
        });

        ib_ch_min.setOnClickListener(v -> {
            if (nbChildren >= 1) {
                nbChildren--;
                tv_ch.setText(String.valueOf(nbChildren));
            }
        });

        btn_done.setOnClickListener(v ->{

            if (callBackListener != null) {
                callBackListener.onGuestSelected(nbAdultes, nbChildren);
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



}