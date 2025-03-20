package com.ingoma.tourism.ui.account;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ingoma.tourism.BookingDetailsActivity;
import com.ingoma.tourism.LoginActivity;
import com.ingoma.tourism.MainActivity;
import com.ingoma.tourism.R;
import com.ingoma.tourism.dialog.ConfirmLogoutDialogFragment;
import com.ingoma.tourism.model.User;
import com.ingoma.tourism.utils.LoginPreferencesManager;


public class AccountFragment extends Fragment {

    private AppCompatTextView tv_change_password;
    private TextView tvUserFullName,tvUserEmail;
    private AppCompatEditText et_first_name,et_last_name,et_phone_number,et_email ;
    private LoginPreferencesManager loginPreferencesManager;
    private Button btn_logout;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View root=inflater.inflate(R.layout.fragment_account, container, false);
        View toolbar = root.findViewById(R.id.toolbar_layout_account);
        paddingStatusBar(toolbar);

        loginPreferencesManager = new LoginPreferencesManager(getContext());
        tv_change_password=root.findViewById(R.id.tv_change_password);
        tvUserFullName=root.findViewById(R.id.tvUserFullName);
        tvUserEmail=root.findViewById(R.id.tvUserEmail);
        et_first_name=root.findViewById(R.id.et_first_name);
        et_last_name=root.findViewById(R.id.et_last_name);
        et_phone_number=root.findViewById(R.id.et_phone_number);
        et_email=root.findViewById(R.id.et_email);
        btn_logout=root.findViewById(R.id.btn_logout);

        displayUserData();

        tv_change_password.setOnClickListener(view -> {

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });

        btn_logout.setOnClickListener(view -> {

            ConfirmLogoutDialogFragment dialogFragment = new ConfirmLogoutDialogFragment(() -> {
                loginPreferencesManager.logout();

                // Restart the activity or navigate to login
                Intent intent = new Intent(requireActivity(), MainActivity.class);
                startActivity(intent);
            });
            dialogFragment.show(getParentFragmentManager(), "dialog_logout");
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public int getStatusBarHeight() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = 0;
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }

        // Convert 10dp to pixels
        int extraHeight = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                10,
                Resources.getSystem().getDisplayMetrics()
        );

        return statusBarHeight + extraHeight;
    }
    private void paddingStatusBar(View layout){
        int statusBarHeight = getStatusBarHeight();
        layout.setPadding(layout.getPaddingLeft(), statusBarHeight, layout.getPaddingRight(), layout.getPaddingBottom());

    }
    private void displayUserData() {

        User user = loginPreferencesManager.getUser();
        if (user != null) {

            tvUserFullName.setText(user.getFirst_name() + " " + user.getLast_name());
            tvUserEmail.setText(user.getEmail());

            et_first_name.setText(user.getFirst_name());
            et_last_name.setText(user.getLast_name());
            et_phone_number.setText(user.getPhone_number());
            et_email.setText(user.getEmail());
        }

    }








}