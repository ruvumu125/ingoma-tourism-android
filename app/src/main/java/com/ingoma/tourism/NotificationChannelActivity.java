package com.ingoma.tourism;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import com.ingoma.tourism.R;
import com.ingoma.tourism.api.BookingService;
import com.ingoma.tourism.api.Retrofit2Client;
import com.ingoma.tourism.api.VerificationApiService;
import com.ingoma.tourism.dialog.SuccessDialogFragment;
import com.ingoma.tourism.model.Booking;
import com.ingoma.tourism.model.SendEmailCode;
import com.ingoma.tourism.model.SendWhatsAppCode;
import com.ingoma.tourism.model.ValidationErrorResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationChannelActivity extends AppCompatActivity {
    private AutoCompleteTextView etSpinner;
    private TextInputLayout mailAdress;
    private LinearLayout layout_whatsapp_number;
    private CountryCodePicker ccp;
    private ConstraintLayout btn_next;
    private ConstraintLayout loadingCartGif;
    private TextView tvContinue;
    private AppCompatEditText edtWhatsappNo;
    private TextInputEditText edtEmailAdress;
    String[] canalOptions = {"Email","WhatsApp"};

    private Retrofit2Client retrofit2Client;
    private VerificationApiService verificationApiService;
    private String preferred_notification_channel="";
    private Boolean valid = true;
    public static NotificationChannelActivity instance;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_notification_channel);

        // This is crucial for proper insets handling
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        View RootLayout = findViewById(R.id.rootLytChannel);
        View toolbar = findViewById(R.id.toolbarChannel);
        paddingStatusBar(toolbar);
        paddingBottomNavigationBar(RootLayout);


        retrofit2Client=new Retrofit2Client(getApplicationContext());
        verificationApiService=retrofit2Client.createService(VerificationApiService.class);

        etSpinner=findViewById(R.id.et_spinner);
        mailAdress=findViewById(R.id.mailAdress);
        layout_whatsapp_number=findViewById(R.id.layout_whatsapp_number);
        edtWhatsappNo=findViewById(R.id.edtWhatsappNo);
        edtEmailAdress=findViewById(R.id.edtEmailAdress);
        btn_next = (ConstraintLayout) findViewById(R.id.btn_next_notification_channel);
        loadingCartGif = (ConstraintLayout) findViewById(R.id.loadingCartGifNotificationChannnel);
        tvContinue = (TextView) findViewById(R.id.continueBtnNotificationChannnel);
        btn_next.setEnabled(false);

        String user_email = getIntent().getStringExtra("user_email");
        if (user_email != null) {
            edtEmailAdress.setText(user_email);
            edtEmailAdress.setEnabled(false);
        }


        ccp = findViewById(R.id.ccp);
        // Map numeric country code to name code
        String countryCode = "+257";  // Country code for the USA
        String countryNameCode = getCountryNameCode(countryCode);

        //Set the default country using the country name code
        if (countryNameCode != null) {
            ccp.setCountryForNameCode(countryNameCode);
        }


        // Set up the adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.item_dropdown,
                canalOptions
        );
        etSpinner.setAdapter(adapter);

        // Optional: Handle item click
        etSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("Email")){
                    mailAdress.setVisibility(View.VISIBLE);
                    layout_whatsapp_number.setVisibility(View.GONE);
                    btn_next.setEnabled(true);
                    preferred_notification_channel="Email";

                } else if (selectedItem.equals("WhatsApp")) {
                    mailAdress.setVisibility(View.GONE);
                    layout_whatsapp_number.setVisibility(View.VISIBLE);
                    btn_next.setEnabled(true);
                    preferred_notification_channel="WhatsApp";
                }
                else {
                    mailAdress.setVisibility(View.GONE);
                    layout_whatsapp_number.setVisibility(View.GONE);
                    btn_next.setEnabled(false);
                    preferred_notification_channel="";
                }
            }
        });

        btn_next.setOnClickListener(view -> {
            String num=ccp.getSelectedCountryCodeWithPlus()+edtWhatsappNo.getText().toString();
            String email=edtEmailAdress.getText().toString();

            loadingCartGif.setVisibility(View.VISIBLE);
            btn_next.setEnabled(false);
            tvContinue.setText("");

            if (preferred_notification_channel.equals("Email")){

                if (TextUtils.isEmpty(email)) {
                    valid = false;

                    loadingCartGif.setVisibility(View.GONE);
                    btn_next.setEnabled(true);
                    tvContinue.setText("Continuer");

                    AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                    dialog.setTitle("Info !");
                    dialog.setMessage("Veuillez saisir votre adresse de téléphone ");
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
                    sendEmailCode(email,preferred_notification_channel);
                }

            } else if (preferred_notification_channel.equals("WhatsApp")) {

                if (TextUtils.isEmpty(edtWhatsappNo.getText().toString())) {
                    valid = false;

                    loadingCartGif.setVisibility(View.GONE);
                    btn_next.setEnabled(true);
                    tvContinue.setText("Continuer");

                    AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                    dialog.setTitle("Info !");
                    dialog.setMessage("Veuillez saisir votre numéro WhatsApp");
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
                    sendWhatsAppCode(num,preferred_notification_channel);
                }
            }
            else{

                AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                dialog.setTitle("Info !");
                dialog.setMessage("Veuillez choisir un canal de notification");
                dialog.setCancelable(true);

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
            }

        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
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

    public void sendEmailCode(String email,String preferred_notification_channel) {

        SendEmailCode sendEmailCode = new SendEmailCode (email);

        // Send the POST request
        Call<SendEmailCode> call = verificationApiService.sendEmailCode(sendEmailCode);
        call.enqueue(new Callback<SendEmailCode>() {
            @Override
            public void onResponse(Call<SendEmailCode> call, Response<SendEmailCode> response) {
                if (response.isSuccessful()) {

                    loadingCartGif.setVisibility(View.GONE);
                    btn_next.setEnabled(true);
                    tvContinue.setText("Continuer");

                    Intent intent = new Intent(NotificationChannelActivity.this, OTPVerifierActivity.class);
                    intent.putExtra("email",email);
                    intent.putExtra("preferred_notification_channel",preferred_notification_channel);
                    intent.putExtra("is_first_time",true);
                    startActivity(intent);

                } else {

                    loadingCartGif.setVisibility(View.GONE);
                    btn_next.setEnabled(true);
                    tvContinue.setText("Continuer");

                    handleValidationError(response);
                }
            }

            @Override
            public void onFailure(Call<SendEmailCode> call, Throwable t) {
                Toast.makeText(NotificationChannelActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("BookingError", t.getMessage());

                loadingCartGif.setVisibility(View.GONE);
                btn_next.setEnabled(true);
                tvContinue.setText("Continuer");
            }
        });
    }

    public void sendWhatsAppCode(String whatsapp_number,String preferred_notification_channel) {

        SendWhatsAppCode sendWhatsAppCode = new SendWhatsAppCode (whatsapp_number);

        // Send the POST request
        Call<SendWhatsAppCode> call = verificationApiService.sendWhatsAppCode(sendWhatsAppCode);
        call.enqueue(new Callback<SendWhatsAppCode>() {
            @Override
            public void onResponse(Call<SendWhatsAppCode> call, Response<SendWhatsAppCode> response) {
                if (response.isSuccessful()) {

                    loadingCartGif.setVisibility(View.GONE);
                    btn_next.setEnabled(true);
                    tvContinue.setText("Continuer");

                    Intent intent = new Intent(NotificationChannelActivity.this, OTPVerifierActivity.class);
                    intent.putExtra("whatsapp_number",whatsapp_number);
                    intent.putExtra("preferred_notification_channel",preferred_notification_channel);
                    intent.putExtra("is_first_time",true);
                    startActivity(intent);

                } else {

                    loadingCartGif.setVisibility(View.GONE);
                    btn_next.setEnabled(true);
                    tvContinue.setText("Continuer");

                    handleValidationError(response);
                }
            }

            @Override
            public void onFailure(Call<SendWhatsAppCode> call, Throwable t) {
                Toast.makeText(NotificationChannelActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("BookingError", t.getMessage());

                loadingCartGif.setVisibility(View.GONE);
                btn_next.setEnabled(true);
                tvContinue.setText("Continuer");
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
                StringBuilder errorMessages = new StringBuilder("Erreurs d'envoi:\n\n");

                for (Map.Entry<String, List<String>> entry : errorResponse.getData().entrySet()) {
                    String field = entry.getKey();
                    List<String> messages = entry.getValue();

                    errorMessages.append("- ").append(field).append(": ");
                    for (String message : messages) {
                        errorMessages.append(message).append(" ");
                    }
                    errorMessages.append("\n");
                }

                if (response.code()==400){
                    // Show the AlertDialog
                    showErrorDialog(errorMessages.toString(),true);
                }
                else{
                    // Show the AlertDialog
                    showErrorDialog(errorMessages.toString(),false);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("An unexpected error occurred.",false);
        }
    }
    private void showErrorDialog(String errorMessages, boolean redirectToActivity) {
        new AlertDialog.Builder(this)
                .setTitle("Erreurs d'envoi")
                .setMessage(errorMessages)
                .setPositiveButton("OK", (dialog, which) -> {
                    if (redirectToActivity) {
                        Intent intent = new Intent(this, OTPVerifierActivity.class);
                        intent.putExtra("email",edtEmailAdress.getText().toString());
                        intent.putExtra("whatsapp_number",ccp.getSelectedCountryCodeWithPlus()+edtWhatsappNo.getText().toString());
                        intent.putExtra("preferred_notification_channel",preferred_notification_channel);
                        intent.putExtra("is_first_time",false);
                        startActivity(intent);
                    }
                    dialog.dismiss(); // Close the dialog
                })
                .setCancelable(false)
                .show();
    }


}