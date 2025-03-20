package com.ingoma.tourism;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.ingoma.tourism.api.LoginService;
import com.ingoma.tourism.api.Retrofit2Client;
import com.ingoma.tourism.dialog.ErrorDialogFragment;
import com.ingoma.tourism.model.ApiErrorResponse;
import com.ingoma.tourism.model.LoginRequest;
import com.ingoma.tourism.model.LoginResponse;
import com.ingoma.tourism.model.User;
import com.ingoma.tourism.utils.LoginPreferencesManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private AppCompatTextView tv_create_account;
    private LoginPreferencesManager loginPreferencesManager;
    private AppCompatEditText et_email,et_password;

    private ConstraintLayout btn_next;
    private ConstraintLayout loadingCartGif;
    private TextView tvContinue;
    private Boolean valid = true;
    private Retrofit2Client retrofit2Client;
    private LoginService loginService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        retrofit2Client=new Retrofit2Client(getApplicationContext());
        loginService= retrofit2Client.createService(LoginService.class);
        loginPreferencesManager= new LoginPreferencesManager(this);

        et_email= findViewById(R.id.etUserEmail);
        et_password=findViewById(R.id.et_password);

        btn_next =findViewById(R.id.btn_next);
        loadingCartGif =findViewById(R.id.loadingCartGif);
        tvContinue =findViewById(R.id.continueBtn);

        btn_next.setOnClickListener(view -> {

            loadingCartGif.setVisibility(View.VISIBLE);
            btn_next.setEnabled(false);
            tvContinue.setText("");

            if (TextUtils.isEmpty(et_email.getText().toString())) {
                valid = false;

                loadingCartGif.setVisibility(View.GONE);
                btn_next.setEnabled(true);
                tvContinue.setText("Continuer");

                AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                dialog.setTitle("Info !");
                dialog.setMessage("Veuillez saisir l'email ");
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
                if (TextUtils.isEmpty(et_password.getText().toString())){
                    valid = false;

                    loadingCartGif.setVisibility(View.GONE);
                    btn_next.setEnabled(true);
                    tvContinue.setText("Continuer");

                    AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                    dialog.setTitle("Info !");
                    dialog.setMessage("Veuillez saisir le mot de passe ");
                    dialog.setCancelable(true);

                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alert = dialog.create();
                    alert.show();

                }
            }

            if(valid){

                String username = et_email.getText().toString();
                String password = et_password.getText().toString();
                login(username, password);
            }
        });

        tv_create_account=findViewById(R.id.tv_create_account);
        tv_create_account.setOnClickListener(view -> {
            Intent intent = new Intent(this, CreateAccountActivity.class);
            startActivity(intent);
        });
    }

    private void login(String email, String password) {
        // Create login request
        LoginRequest loginRequest = new LoginRequest(email, password);

        // Call the login API
        loginService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    LoginResponse.Data data = response.body().getData();
                    String token = data.getToken();
                    User user = data.getUser();

                    if (!user.getRole().equals("customer")){
                        displayErrorMessage("Vous n'êtes pas autorisé à utiliser ce service");
                    }

                    loginPreferencesManager.saveToken(token);
                    loginPreferencesManager.saveUser(user);

                    onLoginSuccess();




                } else {

                    loadingCartGif.setVisibility(View.GONE);
                    btn_next.setEnabled(true);
                    tvContinue.setText("Continuer");

                    // Parse the error response
                    Gson gson = new Gson();
                    ApiErrorResponse apiErrorResponse = gson.fromJson(response.errorBody().charStream(), ApiErrorResponse.class);

                    // Display the error message if it's not null
                    if (apiErrorResponse != null) {
                        displayErrorMessage(apiErrorResponse.getData().getError());
                    }




                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                loadingCartGif.setVisibility(View.GONE);
                btn_next.setEnabled(true);
                tvContinue.setText("Continuer");
            }
        });
    }

    public void displayErrorMessage(String message) {

        ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment(new ErrorDialogFragment.CallBackListener() {
            @Override
            public void onOkClick() {

            }
        });

        Bundle args = new Bundle();
        args.putString("ERROR_MESSAGE", message);
        errorDialogFragment.setArguments(args);

        errorDialogFragment.show(getSupportFragmentManager(), "ErrorDialog");
    }

    private void onLoginSuccess() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish(); // Close LoginActivity and return to MainActivity
    }



}