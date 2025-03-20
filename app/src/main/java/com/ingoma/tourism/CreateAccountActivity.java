package com.ingoma.tourism;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.ingoma.tourism.api.LoginService;
import com.ingoma.tourism.api.Retrofit2Client;
import com.ingoma.tourism.dialog.SuccessDialogFragment;
import com.ingoma.tourism.model.User;
import com.ingoma.tourism.model.ValidationErrorResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAccountActivity extends AppCompatActivity {
    private AppCompatEditText et_first_name,et_last_name,et_mail,et_phone_number;
    private TextInputEditText et_password,et_confirm_password;
    private ConstraintLayout btn_next;
    private ConstraintLayout loadingCartGif;
    private TextView tvContinue;

    private Boolean valid = true;
    private Retrofit2Client retrofit2Client;
    private LoginService loginService;
    private AppCompatTextView tv_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);

        retrofit2Client=new Retrofit2Client(getApplicationContext());
        loginService=retrofit2Client.createService(LoginService.class);

        et_first_name=findViewById(R.id.et_first_name);
        et_last_name=findViewById(R.id.et_last_name);
        et_mail=findViewById(R.id.et_email);
        et_phone_number=findViewById(R.id.et_phone);
        et_password=findViewById(R.id.etPassword);
        et_confirm_password=findViewById(R.id.etConfirmPassword);
        tv_login=findViewById(R.id.tv_login);

        btn_next = (ConstraintLayout) findViewById(R.id.btn_next);
        loadingCartGif = (ConstraintLayout) findViewById(R.id.loadingCartGif);
        tvContinue = (TextView) findViewById(R.id.continueBtn);

        btn_next.setOnClickListener(view -> {

            loadingCartGif.setVisibility(View.VISIBLE);
            btn_next.setEnabled(false);
            tvContinue.setText("");

            if (TextUtils.isEmpty(et_first_name.getText().toString())) {
                valid = false;

                loadingCartGif.setVisibility(View.GONE);
                btn_next.setEnabled(true);
                tvContinue.setText("Continuer");

                AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                dialog.setTitle("Info !");
                dialog.setMessage("Veuillez saisir votre nom ");
                dialog.setCancelable(true);

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                AlertDialog alert = dialog.create();
                alert.show();

            } else {

                valid = true;
                if (TextUtils.isEmpty(et_last_name.getText().toString())){
                    valid = false;

                    loadingCartGif.setVisibility(View.GONE);
                    btn_next.setEnabled(true);
                    tvContinue.setText("Continuer");

                    AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                    dialog.setTitle("Info !");
                    dialog.setMessage("Veuillez saisir votre prénom ");
                    dialog.setCancelable(true);

                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alert = dialog.create();
                    alert.show();

                }
                else {
                    valid=true;
                    if (TextUtils.isEmpty(et_mail.getText().toString())){

                        valid=false;

                        loadingCartGif.setVisibility(View.GONE);
                        btn_next.setEnabled(true);
                        tvContinue.setText("Continuer");

                        AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                        dialog.setTitle("Info !");
                        dialog.setMessage("Veuillez saisir votre email ");
                        dialog.setCancelable(true);

                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                        AlertDialog alert = dialog.create();
                        alert.show();

                    }
                    else{
                        valid=true;

                        if (TextUtils.isEmpty(et_phone_number.getText().toString())){

                            valid=false;

                            loadingCartGif.setVisibility(View.GONE);
                            btn_next.setEnabled(true);
                            tvContinue.setText("Continuer");

                            AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                            dialog.setTitle("Info !");
                            dialog.setMessage("Veuillez saisir votre numéro de téléphone ");
                            dialog.setCancelable(true);

                            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                            AlertDialog alert = dialog.create();
                            alert.show();



                        }
                        else {
                            valid=true;

                            if (TextUtils.isEmpty(et_password.getText().toString())){

                                valid=false;

                                loadingCartGif.setVisibility(View.GONE);
                                btn_next.setEnabled(true);
                                tvContinue.setText("Continuer");

                                AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                                dialog.setTitle("Info !");
                                dialog.setMessage("Veuillez saisir votre mot de passe ");
                                dialog.setCancelable(true);

                                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                                AlertDialog alert = dialog.create();
                                alert.show();



                            }
                            else {
                                valid=true;

                                if (TextUtils.isEmpty(et_confirm_password.getText().toString())){

                                    valid=false;

                                    loadingCartGif.setVisibility(View.GONE);
                                    btn_next.setEnabled(true);
                                    tvContinue.setText("Continuer");

                                    AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                                    dialog.setTitle("Info !");
                                    dialog.setMessage("Veuillez saisir confirmer votre mot de passe ");
                                    dialog.setCancelable(true);

                                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                                    AlertDialog alert = dialog.create();
                                    alert.show();
                                }
                                else {

                                    valid =true;

                                }
                            }

                        }
                    }
                }
            }

            if(valid){

                String first_name = et_first_name.getText().toString();
                String last_name = et_last_name.getText().toString();
                String email=et_mail.getText().toString();
                String phone_number=et_phone_number.getText().toString();
                String password=et_password.getText().toString();
                String confirm_password=et_confirm_password.getText().toString();

                registrer(first_name,last_name,email,phone_number,password,confirm_password);
            }
        });

        tv_login.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }

    public void registrer(String first_name,String last_name,String email,String phone_number,String password,String confirm_password) {

        User user = new User (first_name,last_name,email,phone_number,password,confirm_password);

        Call<Void> call = loginService.register(user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {

                    loadingCartGif.setVisibility(View.GONE);
                    btn_next.setEnabled(true);
                    tvContinue.setText("Continuer");

                    SuccessDialogFragment successDialogFragment = new SuccessDialogFragment(new SuccessDialogFragment.CallBackListener() {
                        @Override
                        public void onOkClick() {

                            Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    // Create a Bundle to pass the argument
                    Bundle args = new Bundle();
                    args.putString("success_message", "Compte crée avec succès");
                    successDialogFragment.setArguments(args);
                    successDialogFragment.show(getSupportFragmentManager(), "SuccessDialog");


                } else {
                    // Handle error response, response code is not 2xx
                    Log.e("API Error", "Response code: " + response.code());
                    Toast.makeText(CreateAccountActivity.this, "nmmmmmmmm", Toast.LENGTH_SHORT).show();

                    loadingCartGif.setVisibility(View.GONE);
                    btn_next.setEnabled(true);
                    tvContinue.setText("Continuer");

                    handleValidationError(response);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
                Log.e("API Failure", t.getMessage());

                loadingCartGif.setVisibility(View.GONE);
                btn_next.setEnabled(true);
                tvContinue.setText("Continuer");

                Toast.makeText(CreateAccountActivity.this, "ngwahooo", Toast.LENGTH_SHORT).show();

                //displayErrorMessage(t.getMessage());
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
                StringBuilder errorMessages = new StringBuilder("Validation Errors:\n");

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