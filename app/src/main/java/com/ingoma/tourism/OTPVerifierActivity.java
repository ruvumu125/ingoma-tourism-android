package com.ingoma.tourism;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.ingoma.tourism.api.Retrofit2Client;
import com.ingoma.tourism.api.VerificationApiService;
import com.ingoma.tourism.model.SendEmailCode;
import com.ingoma.tourism.model.ValidationErrorResponse;
import com.ingoma.tourism.model.VerifyEmail;
import com.ingoma.tourism.model.VerifyWhatsApp;

import java.util.List;
import java.util.Map;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("MissingInflatedId")
public class OTPVerifierActivity extends AppCompatActivity {

    private OtpTextView otpTextView;
    private TextView tv_email_or_mobile_number,otp_verifier_send_code_counter,txtLabel;
    private ImageView otp_verifier_change_number_txt;
    private Retrofit2Client retrofit2Client;
    private VerificationApiService verificationApiService;

    private CountDownTimer countDownTimer;
    private long timeInSeconds = 300;
    private long endTimeMillis;
    private SharedPreferences preferences;

    private static final String PREFS_NAME = "CountdownPrefs";
    private static final String PREF_END_TIME = "countdownEndTime";

    private ConstraintLayout btn_next;
    private ConstraintLayout loadingCartGif;
    private TextView tvContinue;
    private String otp_code="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverifier);


        // This is crucial for proper insets handling
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        View RootLayout = findViewById(R.id.rootLytOtp);
        View toolbar = findViewById(R.id.toolbarOtp);
        paddingStatusBar(toolbar);
        paddingBottomNavigationBar(RootLayout);

        retrofit2Client=new Retrofit2Client(getApplicationContext());
        verificationApiService=retrofit2Client.createService(VerificationApiService.class);
        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        String preferred_notification_channel = getIntent().getStringExtra("preferred_notification_channel");
        String email= getIntent().getStringExtra("email");
        String whatsapp_number = getIntent().getStringExtra("whatsapp_number");
        Boolean is_first_time = getIntent().getBooleanExtra("is_first_time",true);


        otpTextView = findViewById(R.id.otp_view);
        tv_email_or_mobile_number = findViewById(R.id.tv_email_or_mobile_number);
        txtLabel = findViewById(R.id.txtLabel);
        otp_verifier_change_number_txt = findViewById(R.id.otp_verifier_change_number_txt);
        otp_verifier_send_code_counter = findViewById(R.id.otp_verifier_send_code_counter);

        //start timer
        if (is_first_time) {
            // First time: reset timer
            endTimeMillis = System.currentTimeMillis() + timeInSeconds * 1000;
            saveEndTime(endTimeMillis);
        } else {
            // Not first time: load saved timer
            endTimeMillis = preferences.getLong(PREF_END_TIME, -1);

            if (endTimeMillis == -1) {
                // Safety: if no saved time, restart fresh
                endTimeMillis = System.currentTimeMillis() + timeInSeconds * 1000;
                saveEndTime(endTimeMillis);
            }
        }

        startTimerBasedOnEndTime();

        //end timer



        btn_next = (ConstraintLayout) findViewById(R.id.btn_next_otp_verifier);
        loadingCartGif = (ConstraintLayout) findViewById(R.id.loadingCartGifOtpVerifier);
        tvContinue = (TextView) findViewById(R.id.continueBtnOtpVerifier);
        btn_next.setEnabled(false);

        if (preferred_notification_channel != null) {

            if (preferred_notification_channel.equals("Email")){
                tv_email_or_mobile_number.setText(email);
                otp_verifier_change_number_txt.setVisibility(View.GONE);
                txtLabel.setText("Saisissez votre OTP à 4 chiffres. Nous avons envoyé un email avec l'OTP à");

            }
            else {
                tv_email_or_mobile_number.setText(whatsapp_number);
                otp_verifier_change_number_txt.setVisibility(View.VISIBLE);
                txtLabel.setText("Saisissez votre OTP à 4 chiffres. Nous avons envoyé un message WhatsApp  avec l'OTP à");

            }
        }

        otp_verifier_change_number_txt.setOnClickListener(view -> {
            finish();
        });

        otpTextView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                // fired when user types something in the Otpbox
            }
            @Override
            public void onOTPComplete(String otp) {
                btn_next.setEnabled(true);
                otp_code=otp;
                // fired when user has entered the OTP fully.
                //Toast.makeText(OTPVerifierActivity.this, "The OTP is " + otp,  Toast.LENGTH_SHORT).show();
            }
        });

        btn_next.setOnClickListener(view -> {

            loadingCartGif.setVisibility(View.VISIBLE);
            btn_next.setEnabled(false);
            tvContinue.setText("");

            if (preferred_notification_channel.equals("Email")){

                verifyEmail(otp_code,"email");

            }else{

                verifyWhatsApp(otp_code,"whatsapp");
            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimerBasedOnEndTime(); // recalculate when waking up!
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }


    private void paddingStatusBar(View view) {
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            int statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top;
            v.setPadding(v.getPaddingLeft(),
                    statusBarHeight,
                    v.getPaddingRight(),
                    v.getPaddingBottom());
            return insets;
        });
        ViewCompat.requestApplyInsets(view);
    }

    private void paddingBottomNavigationBar(View layout) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewCompat.setOnApplyWindowInsetsListener(layout, (v, insets) -> {
                int bottomInset = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom;
                v.setPadding(v.getPaddingLeft(),
                        v.getPaddingTop(),
                        v.getPaddingRight(),
                        bottomInset);
                return insets;
            });
            ViewCompat.requestApplyInsets(layout);
        } else {
            // Fallback for very old devices
            int navBarHeight = getNavigationBarHeightLegacy(layout.getContext());
            layout.setPadding(layout.getPaddingLeft(),
                    layout.getPaddingTop(),
                    layout.getPaddingRight(),
                    navBarHeight);
        }
    }

    @SuppressLint("InternalInsetResource")
    private int getNavigationBarHeightLegacy(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resourceId > 0 ? resources.getDimensionPixelSize(resourceId) : 0;
    }



    private void startTimerBasedOnEndTime() {
        long remainingMillis = endTimeMillis - System.currentTimeMillis();

        if (remainingMillis <= 0) {
            // Timer expired
            otp_verifier_send_code_counter.setText("expired");
            clearSavedEndTime();
            return;
        }

        long remainingSeconds = remainingMillis / 1000;
        countDownTimer = startOtpCountdown(remainingSeconds, otp_verifier_send_code_counter);
    }

    private CountDownTimer startOtpCountdown(long timeInSeconds, TextView tvSeconds) {
        CountDownTimer countDownTimer = new CountDownTimer(timeInSeconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                tvSeconds.setText(secondsRemaining + " sec");
            }

            @Override
            public void onFinish() {
                tvSeconds.setText("expired");
                finish();
            }
        };

        countDownTimer.start();
        return countDownTimer;
    }

    private void saveEndTime(long endTimeMillis) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(PREF_END_TIME, endTimeMillis);
        editor.apply();
    }

    private void clearSavedEndTime() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(PREF_END_TIME);
        editor.apply();
    }


    public void verifyEmail(String code,String preferred_notification_channel) {

        VerifyEmail verifyEmail = new VerifyEmail (code,preferred_notification_channel);

        // Send the POST request
        Call<VerifyEmail> call = verificationApiService.verifyEmail(verifyEmail);
        call.enqueue(new Callback<VerifyEmail>() {
            @Override
            public void onResponse(Call<VerifyEmail> call, Response<VerifyEmail> response) {
                if (response.isSuccessful()) {

                    loadingCartGif.setVisibility(View.GONE);
                    btn_next.setEnabled(true);
                    tvContinue.setText("Vérifier");

                    if (NotificationChannelActivity.instance != null) {
                        NotificationChannelActivity.instance.finish(); // finish VerifEmailActivity
                    }
                    finish(); // finish OtpActivity itself


                } else {

                    loadingCartGif.setVisibility(View.GONE);
                    btn_next.setEnabled(true);
                    tvContinue.setText("Vérifier");

                    handleValidationError(response);
                }
            }

            @Override
            public void onFailure(Call<VerifyEmail> call, Throwable t) {
                Toast.makeText(OTPVerifierActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("BookingError", t.getMessage());

                loadingCartGif.setVisibility(View.GONE);
                btn_next.setEnabled(true);
                tvContinue.setText("Vérifier");
            }
        });
    }

    public void verifyWhatsApp(String code,String preferred_notification_channel) {

        VerifyWhatsApp verifyWhatsApp = new VerifyWhatsApp (code,preferred_notification_channel);

        // Send the POST request
        Call<VerifyWhatsApp> call = verificationApiService.verifyWhatsApp(verifyWhatsApp);
        call.enqueue(new Callback<VerifyWhatsApp>() {
            @Override
            public void onResponse(Call<VerifyWhatsApp> call, Response<VerifyWhatsApp> response) {
                if (response.isSuccessful()) {

                    loadingCartGif.setVisibility(View.GONE);
                    btn_next.setEnabled(true);
                    tvContinue.setText("Vérifier");


                } else {

                    loadingCartGif.setVisibility(View.GONE);
                    btn_next.setEnabled(true);
                    tvContinue.setText("Vérifier");

                    handleValidationError(response);
                }
            }

            @Override
            public void onFailure(Call<VerifyWhatsApp> call, Throwable t) {
                Toast.makeText(OTPVerifierActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("BookingError", t.getMessage());

                loadingCartGif.setVisibility(View.GONE);
                btn_next.setEnabled(true);
                tvContinue.setText("Vérifier");
            }
        });
    }

    private void handleValidationError(Response<?> response) {
        try {
            String errorBody = response.errorBody().string(); // Raw error response
            Gson gson = new Gson();
            ValidationErrorResponse errorResponse = gson.fromJson(errorBody, ValidationErrorResponse.class);

            if (errorResponse != null && errorResponse.getData() != null) {
                // Build a dynamic error message
                StringBuilder errorMessages = new StringBuilder("Erreurs de vérification :\n\n");

                for (Map.Entry<String, List<String>> entry : errorResponse.getData().entrySet()) {
                    String field = entry.getKey();
                    List<String> messages = entry.getValue();

                    errorMessages.append("- ").append(field).append(": ");
                    for (String message : messages) {
                        errorMessages.append(message).append(" ");
                    }
                    errorMessages.append("\n");
                }

                // Show the AlertDialog
                showErrorDialog(errorMessages.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("An unexpected error occurred.");
        }
    }
    private void showErrorDialog(String errorMessages) {
        new AlertDialog.Builder(this)
                .setTitle("Validation Errors")
                .setMessage(errorMessages)
                .setPositiveButton("OK", null)
                .show();
    }

}