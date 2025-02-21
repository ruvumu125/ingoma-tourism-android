package com.ingoma.tourism;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationBarView;
import com.ingoma.tourism.databinding.ActivityMainBinding;
import com.ingoma.tourism.ui.dashboard.DashboardFragment;
import com.ingoma.tourism.ui.home.HomeFragment;
import com.ingoma.tourism.ui.notifications.NotificationsFragment;
import com.ingoma.tourism.R;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the BottomNavigationView
        BottomNavigationView bottomNav = findViewById(R.id.nav_view);

        // Find the NavController from the NavHostFragment
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // Link BottomNavigationView with NavController
        NavigationUI.setupWithNavController(bottomNav, navController);


    }

}