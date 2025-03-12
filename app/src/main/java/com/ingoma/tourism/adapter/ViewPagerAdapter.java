package com.ingoma.tourism.adapter;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ingoma.tourism.ui.bookings.HotelBookingsFragment;
import com.ingoma.tourism.ui.bookings.MaisonPassageBookingsFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment); // Pass Fragment, not Activity
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {


        switch (position) {
            case 0:
                return new HotelBookingsFragment();
            case 1:
                return new MaisonPassageBookingsFragment();
            default:
                return new HotelBookingsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Number of tabs
    }
}

