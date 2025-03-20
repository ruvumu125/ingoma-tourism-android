package com.ingoma.tourism.dialog;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingoma.tourism.R;


import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ingoma.tourism.R;

public class ErrorDialogFragment extends DialogFragment {

    private TextView error_message,error_message_array;
    private AppCompatButton btn_error_done;

    private CallBackListener callBackListener;

    public ErrorDialogFragment(CallBackListener callBackListener) {
        this.callBackListener = callBackListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_error_dialog, container, false);

        Bundle args = getArguments();
        String ERROR_MESSAGE = args != null ? args.getString("ERROR_MESSAGE") : "";
        String ERROR_MESSAGE_ARRAY = args != null ? args.getString("list_error") : "";
        error_message=(TextView) view.findViewById(R.id.error_message);
        error_message_array=(TextView) view.findViewById(R.id.error_message_array);
        btn_error_done=(AppCompatButton) view.findViewById(R.id.btn_error_done);

        error_message.setText(ERROR_MESSAGE);
        error_message_array.setText(ERROR_MESSAGE_ARRAY);
        btn_error_done.setOnClickListener(new View.OnClickListener() {
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
