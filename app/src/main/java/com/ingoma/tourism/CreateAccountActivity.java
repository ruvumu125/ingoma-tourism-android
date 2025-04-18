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
import com.hbb20.CountryCodePicker;
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
    private AppCompatEditText et_first_name,et_last_name,et_mail;
    private TextInputEditText et_password,et_confirm_password;
    private ConstraintLayout btn_next;
    private ConstraintLayout loadingCartGif;
    private TextView tvContinue;

    private Boolean valid = true;
    private Retrofit2Client retrofit2Client;
    private LoginService loginService;
    private AppCompatTextView tv_login;
    private CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);

        retrofit2Client=new Retrofit2Client(getApplicationContext());
        loginService=retrofit2Client.createService(LoginService.class);
        ccp = findViewById(R.id.ccp);
        // Map numeric country code to name code
        //String countryCode = "+257";  // Country code for the USA
        //String countryNameCode = getCountryNameCode(countryCode);

        // Set the default country using the country name code
        //if (countryNameCode != null) {
            //ccp.setCountryForNameCode(countryNameCode);
        //}

        //String selectedCountryCode = ccp.getSelectedCountryCodeWithPlus();

        et_first_name=findViewById(R.id.et_first_name);
        et_last_name=findViewById(R.id.et_last_name);
        et_mail=findViewById(R.id.et_email);
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

            if(valid){

                String first_name = et_first_name.getText().toString();
                String last_name = et_last_name.getText().toString();
                String email=et_mail.getText().toString();
                String password=et_password.getText().toString();
                String confirm_password=et_confirm_password.getText().toString();

                registrer(first_name,last_name,email,password,confirm_password);
            }
        });

        tv_login.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }

    public void registrer(String first_name,String last_name,String email,String password,String confirm_password) {

        User user = new User (first_name,last_name,email,password,confirm_password);

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

    private String getCountryNameCode(String countryCode) {
        switch (countryCode) {
            case "+1":
                return "US";  // United States
            case "+7":
                return "RU";  // Russia
            case "+20":
                return "EG";  // Egypt
            case "+30":
                return "GR";  // Greece
            case "+31":
                return "NL";  // Netherlands
            case "+32":
                return "BE";  // Belgium
            case "+33":
                return "FR";  // France
            case "+34":
                return "ES";  // Spain
            case "+39":
                return "IT";  // Italy
            case "+40":
                return "RO";  // Romania
            case "+41":
                return "CH";  // Switzerland
            case "+42":
                return "AT";  // Austria
            case "+43":
                return "PL";  // Poland
            case "+44":
                return "GB";  // United Kingdom
            case "+45":
                return "DK";  // Denmark
            case "+46":
                return "SE";  // Sweden
            case "+47":
                return "NO";  // Norway
            case "+48":
                return "PL";  // Poland
            case "+49":
                return "DE";  // Germany
            case "+52":
                return "MX";  // Mexico
            case "+53":
                return "CU";  // Cuba
            case "+54":
                return "AR";  // Argentina
            case "+55":
                return "BR";  // Brazil
            case "+56":
                return "CL";  // Chile
            case "+57":
                return "CO";  // Colombia
            case "+58":
                return "VE";  // Venezuela
            case "+60":
                return "MY";  // Malaysia
            case "+61":
                return "AU";  // Australia
            case "+62":
                return "ID";  // Indonesia
            case "+63":
                return "PH";  // Philippines
            case "+64":
                return "NZ";  // New Zealand
            case "+65":
                return "SG";  // Singapore
            case "+66":
                return "TH";  // Thailand
            case "+81":
                return "JP";  // Japan
            case "+82":
                return "KR";  // South Korea
            case "+84":
                return "VN";  // Vietnam
            case "+86":
                return "CN";  // China
            case "+90":
                return "TR";  // Turkey
            case "+91":
                return "IN";  // India
            case "+92":
                return "PK";  // Pakistan
            case "+93":
                return "AF";  // Afghanistan
            case "+94":
                return "LK";  // Sri Lanka
            case "+95":
                return "MM";  // Myanmar
            case "+98":
                return "IR";  // Iran
            case "+212":
                return "MA";  // Morocco
            case "+213":
                return "DZ";  // Algeria
            case "+216":
                return "TN";  // Tunisia
            case "+218":
                return "LY";  // Libya
            case "+220":
                return "GM";  // Gambia
            case "+221":
                return "SN";  // Senegal
            case "+222":
                return "MR";  // Mauritania
            case "+223":
                return "ML";  // Mali
            case "+224":
                return "GN";  // Guinea
            case "+225":
                return "CI";  // Ivory Coast
            case "+226":
                return "BF";  // Burkina Faso
            case "+227":
                return "NE";  // Niger
            case "+228":
                return "TG";  // Togo
            case "+229":
                return "BJ";  // Benin
            case "+230":
                return "MU";  // Mauritius
            case "+231":
                return "LR";  // Liberia
            case "+232":
                return "SL";  // Sierra Leone
            case "+233":
                return "GH";  // Ghana
            case "+234":
                return "NG";  // Nigeria
            case "+235":
                return "CM";  // Cameroon
            case "+236":
                return "CF";  // Central African Republic
            case "+237":
                return "TD";  // Chad
            case "+238":
                return "CV";  // Cape Verde
            case "+239":
                return "ST";  // São Tomé and Príncipe
            case "+240":
                return "GQ";  // Equatorial Guinea
            case "+241":
                return "GA";  // Gabon
            case "+242":
                return "CG";  // Republic of the Congo
            case "+243":
                return "CD";  // Democratic Republic of the Congo
            case "+244":
                return "AO";  // Angola
            case "+245":
                return "GW";  // Guinea-Bissau
            case "+246":
                return "IO";  // British Indian Ocean Territory
            case "+247":
                return "AC";  // Ascension Island
            case "+248":
                return "SC";  // Seychelles
            case "+249":
                return "SD";  // Sudan
            case "+250":
                return "SS";  // South Sudan
            case "+251":
                return "ET";  // Ethiopia
            case "+252":
                return "SO";  // Somalia
            case "+253":
                return "DJ";  // Djibouti
            case "+254":
                return "KE";  // Kenya
            case "+255":
                return "TZ";  // Tanzania
            case "+256":
                return "UG";  // Uganda
            case "+257":
                return "BI";  // Burundi
            case "+258":
                return "MZ";  // Mozambique
            case "+260":
                return "ZM";  // Zambia
            case "+261":
                return "MW";  // Malawi
            case "+262":
                return "RE";  // Réunion (France)
            case "+263":
                return "ZW";  // Zimbabwe
            case "+264":
                return "NA";  // Namibia
            case "+265":
                return "ZA";  // South Africa
            case "+266":
                return "LS";  // Lesotho
            case "+267":
                return "BW";  // Botswana
            case "+268":
                return "SZ";  // Eswatini
            case "+269":
                return "KM";  // Comoros
            case "+290":
                return "SH";  // Saint Helena
            case "+291":
                return "ER";  // Eritrea
            case "+292":
                return "DJ";  // Djibouti
            case "+350":
                return "GI";  // Gibraltar
            case "+351":
                return "PT";  // Portugal
            case "+352":
                return "LU";  // Luxembourg
            case "+353":
                return "IE";  // Ireland
            case "+354":
                return "IS";  // Iceland
            case "+355":
                return "AL";  // Albania
            case "+356":
                return "MT";  // Malta
            case "+357":
                return "CY";  // Cyprus
            case "+358":
                return "FI";  // Finland
            case "+359":
                return "BG";  // Bulgaria
            case "+370":
                return "LT";  // Lithuania
            case "+371":
                return "LV";  // Latvia
            case "+372":
                return "EE";  // Estonia
            case "+373":
                return "MD";  // Moldova
            case "+374":
                return "AM";  // Armenia
            case "+375":
                return "BY";  // Belarus
            case "+376":
                return "AD";  // Andorra
            case "+377":
                return "MC";  // Monaco
            case "+378":
                return "SM";  // San Marino
            case "+379":
                return "VA";  // Vatican City
            case "+380":
                return "UA";  // Ukraine
            case "+381":
                return "RS";  // Serbia
            case "+382":
                return "ME";  // Montenegro
            case "+383":
                return "XK";  // Kosovo
            case "+385":
                return "HR";  // Croatia
            case "+386":
                return "SI";  // Slovenia
            case "+387":
                return "BA";  // Bosnia and Herzegovina
            case "+388":
                return "MK";  // North Macedonia
            case "+389":
                return "AL";  // Albania
            default:
                return null;  // Country code not found
        }
    }



}