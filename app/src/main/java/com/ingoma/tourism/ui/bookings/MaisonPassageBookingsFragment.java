package com.ingoma.tourism.ui.bookings;

import android.os.Bundle;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.ingoma.tourism.R;
import com.ingoma.tourism.adapter.BookingsListAdapter;
import com.ingoma.tourism.api.BookingService;
import com.ingoma.tourism.api.Retrofit2Client;
import com.ingoma.tourism.model.BookingList;
import com.ingoma.tourism.model.BookingListResponse;
import com.ingoma.tourism.model.User;
import com.ingoma.tourism.utils.LoginPreferencesManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MaisonPassageBookingsFragment extends Fragment {

    private boolean isLoading = false;
    private int currentPage = 1;
    private int totalPage = 1;
    private int pageSize = 10;
    private List<BookingList> bookingList = new ArrayList<>();
    private BookingsListAdapter hotelBookingAdapter;

    private RecyclerView recyclerView;
    private NestedScrollView noDataFound_error_skeleton_holder;
    private ShimmerFrameLayout skletonPrincipale;
    private LinearLayout error_section;
    private LinearLayoutCompat no_data_found;

    private LoginPreferencesManager loginPreferencesManager;
    private Retrofit2Client retrofit2Client;
    private BookingService bookingService;
    private Long idUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View root=inflater.inflate(R.layout.fragment_maison_passage_bookings, container, false);

        loginPreferencesManager = new LoginPreferencesManager(getContext());

        //user info
        User user = loginPreferencesManager.getUser();
        if (user != null) {
            idUser=user.getId();

        }

        retrofit2Client=new Retrofit2Client(getContext());
        bookingService = retrofit2Client.createService(BookingService.class);

        recyclerView = root.findViewById(R.id.rv_categories_guest_house);
        noDataFound_error_skeleton_holder = root.findViewById(R.id.nested_scroll_view_guest_house);
        skletonPrincipale= root.findViewById(R.id.shimmerGuestHouse);
        error_section= root.findViewById(R.id.error_section_guest_house);
        no_data_found= root.findViewById(R.id.noResult_layout_guest_house);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        hotelBookingAdapter = new BookingsListAdapter(getContext(),bookingList);
        recyclerView.setAdapter(hotelBookingAdapter);

        fetchGuestHouseBookings(idUser,pageSize,currentPage);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {  // Scrolling down
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    if (!isLoading && currentPage < totalPage) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                            loadMoreHotels();
                        }
                    }
                }
            }
        });

        return root;
    }

    private void fetchGuestHouseBookings(Long userId,int perPage,int page) {
        isLoading = true;

        recyclerView.setVisibility(View.GONE);
        noDataFound_error_skeleton_holder.setVisibility(View.VISIBLE);
        skletonPrincipale.setVisibility(View.VISIBLE);
        error_section.setVisibility(View.GONE);
        no_data_found.setVisibility(View.GONE);

        bookingService.getUserGuestHouseBookings(userId,perPage,page).enqueue(new Callback<BookingListResponse>() {
            @Override
            public void onResponse(Call<BookingListResponse> call, Response<BookingListResponse> response) {
                isLoading = false;
                if (response.isSuccessful() && response.body() != null) {

                    if (!response.body().getData().isEmpty()){

                        recyclerView.setVisibility(View.VISIBLE);
                        noDataFound_error_skeleton_holder.setVisibility(View.GONE);
                        skletonPrincipale.setVisibility(View.GONE);
                        error_section.setVisibility(View.GONE);
                        no_data_found.setVisibility(View.GONE);

                        totalPage = response.body().getPagination().getTotal_pages();
                        bookingList.addAll(response.body().getData());
                        hotelBookingAdapter.notifyDataSetChanged();

                    }
                    else{
                        recyclerView.setVisibility(View.GONE);
                        noDataFound_error_skeleton_holder.setVisibility(View.VISIBLE);
                        skletonPrincipale.setVisibility(View.GONE);
                        error_section.setVisibility(View.GONE);
                        no_data_found.setVisibility(View.VISIBLE);
                    }

                } else {

                    recyclerView.setVisibility(View.GONE);
                    noDataFound_error_skeleton_holder.setVisibility(View.VISIBLE);
                    skletonPrincipale.setVisibility(View.GONE);
                    error_section.setVisibility(View.VISIBLE);
                    no_data_found.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<BookingListResponse> call, Throwable t) {
                isLoading = false;

                recyclerView.setVisibility(View.GONE);
                noDataFound_error_skeleton_holder.setVisibility(View.VISIBLE);
                skletonPrincipale.setVisibility(View.GONE);
                error_section.setVisibility(View.VISIBLE);
                no_data_found.setVisibility(View.GONE);
            }
        });
    }

    private void loadMoreHotels() {
        isLoading = true;
        hotelBookingAdapter.showLoading();
        new Handler().postDelayed(() -> {
            currentPage++;
            bookingService.getUserGuestHouseBookings(idUser, pageSize,currentPage).enqueue(new Callback<BookingListResponse>() {
                @Override
                public void onResponse(Call<BookingListResponse> call, Response<BookingListResponse> response) {
                    hotelBookingAdapter.hideLoading();
                    isLoading = false;
                    if (response.isSuccessful() && response.body() != null) {
                        hotelBookingAdapter.addHotels(response.body().getData());
                    }
                }

                @Override
                public void onFailure(Call<BookingListResponse> call, Throwable t) {
                    hotelBookingAdapter.hideLoading();
                    isLoading = false;

                    recyclerView.setVisibility(View.GONE);
                    noDataFound_error_skeleton_holder.setVisibility(View.VISIBLE);
                    skletonPrincipale.setVisibility(View.GONE);
                    error_section.setVisibility(View.VISIBLE);
                    no_data_found.setVisibility(View.GONE);
                }
            });
        }, 2000);
    }
}