package com.ingoma.tourism.ui.bookings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingoma.tourism.R;
import com.ingoma.tourism.adapter.HotelBookingAdapter;
import com.ingoma.tourism.adapter.MaisonPassageBookingAdapter;
import com.ingoma.tourism.model.HotelBooking;

import java.util.ArrayList;
import java.util.List;


public class MaisonPassageBookingsFragment extends Fragment {

    private RecyclerView recyclerView;
    private MaisonPassageBookingAdapter adapter;
    private List<HotelBooking> bookingList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View root=inflater.inflate(R.layout.fragment_maison_passage_bookings, container, false);

        recyclerView = root.findViewById(R.id.rv_categories_guest_house);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Sample Data
        bookingList = new ArrayList<>();
        bookingList.add(new HotelBooking("Grand Hotel", "2025-03-15", "2025-03-20", 5, 2));
        bookingList.add(new HotelBooking("Sunset Resort", "2025-04-10", "2025-04-14", 4, 3));
        bookingList.add(new HotelBooking("Mountain Lodge", "2025-05-05", "2025-05-08", 3, 1));
        bookingList.add(new HotelBooking("Ocean View Inn", "2025-06-01", "2025-06-07", 6, 2));
        bookingList.add(new HotelBooking("Grand Hotel", "2025-03-15", "2025-03-20", 5, 2));
        bookingList.add(new HotelBooking("Sunset Resort", "2025-04-10", "2025-04-14", 4, 3));
        bookingList.add(new HotelBooking("Mountain Lodge", "2025-05-05", "2025-05-08", 3, 1));
        bookingList.add(new HotelBooking("Ocean View Inn", "2025-06-01", "2025-06-07", 6, 2));

        adapter = new MaisonPassageBookingAdapter(bookingList);
        recyclerView.setAdapter(adapter);

        return root;
    }
}