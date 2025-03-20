package com.ingoma.tourism.dialog;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.ingoma.tourism.R;


public class ConfirmLogoutDialogFragment extends DialogFragment {

    private CallBackListener callBackListener;
    private AppCompatButton btn_dialog_common_tips_cancel;
    private Button btn_dialog_common_tips_ok;
    private ImageButton btn_close;

    public ConfirmLogoutDialogFragment(CallBackListener callBackListener) {
        this.callBackListener = callBackListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View root=inflater.inflate(R.layout.fragment_confirm_logout_dialog, container, false);

        btn_dialog_common_tips_ok=(Button) root.findViewById(R.id.btn_done);
        btn_dialog_common_tips_cancel=(AppCompatButton) root.findViewById(R.id.btn_delete);
        btn_close=(ImageButton) root.findViewById(R.id.btn_close);

        btn_dialog_common_tips_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(callBackListener != null)
                    callBackListener.onLogout();
                dismissAllowingStateLoss();

            }
        });

        btn_dialog_common_tips_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissAllowingStateLoss();
            }
        });

        return root;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // Prevent the dialog from being dismissed when clicking outside
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    public interface CallBackListener {
        void onLogout();
    }
}