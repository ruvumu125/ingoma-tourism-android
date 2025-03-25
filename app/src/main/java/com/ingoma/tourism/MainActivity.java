package com.ingoma.tourism;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ingoma.tourism.utils.LoginPreferencesManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private NavController navController;
    private LoginPreferencesManager loginPreferencesManager;
    private static final int LOGIN_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginPreferencesManager = new LoginPreferencesManager(this);

        // Find the BottomNavigationView
        bottomNav = findViewById(R.id.nav_view);

// Find the NavController from the NavHostFragment
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

// Handle item selection manually
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_account || itemId == R.id.navigation_dashboard || itemId == R.id.navigation_notifications) {
                if (!loginPreferencesManager.isLoggedIn()) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    loginActivityLauncher.launch(intent);
                    return false; // Prevent navigation
                }
            }

            // Navigate manually using NavController
            navController.navigate(itemId);
            return true;
        });

    }

    private final ActivityResultLauncher<Intent> loginActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    navController.navigate(R.id.navigation_account);
                    // ✅ Fix: Ensure BottomNavigationView highlights Account icon
                    bottomNav.setSelectedItemId(R.id.navigation_account);
                }
            }
    );

}