package com.ingoma.tourism.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.ingoma.tourism.R;


public class SuccessDialogFragment extends DialogFragment {

    private Button btn_done;
    private CallBackListener callBackListener;
    private TextView tv_success_message;

    public SuccessDialogFragment(CallBackListener callBackListener) {
        this.callBackListener = callBackListener;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_success_dialog, container, false);


        btn_done=(Button) view.findViewById(R.id.btn_done);
        tv_success_message=(TextView) view.findViewById(R.id.tv_success_message);

        // Retrieve arguments and set the TextView value
        if (getArguments() != null) {
            String message = getArguments().getString("success_message");
            tv_success_message.setText(message);
        }

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(callBackListener != null)
                    callBackListener.onOkClick();
                dismissAllowingStateLoss();
            }
        });

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // Prevent the dialog from being dismissed when clicking outside
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    public interface CallBackListener {
        void onOkClick();
    }
}