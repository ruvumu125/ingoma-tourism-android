package com.ingoma.tourism;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

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