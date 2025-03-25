package com.ingoma.tourism.ui.notifications;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.ingoma.tourism.R;
import com.ingoma.tourism.adapter.BookingsListAdapter;
import com.ingoma.tourism.adapter.NotificationAdapter;
import com.ingoma.tourism.api.BookingService;
import com.ingoma.tourism.api.NotificationApiService;
import com.ingoma.tourism.api.Retrofit2Client;
import com.ingoma.tourism.model.Notification;
import com.ingoma.tourism.model.NotificationResponse;
import com.ingoma.tourism.model.User;
import com.ingoma.tourism.utils.LoginPreferencesManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {

    private boolean isLoading = false;
    private int currentPage = 1;
    private int totalPage = 1;
    private int pageSize = 10;
    private List<Notification> notifications = new ArrayList<>();
    private NotificationAdapter notificationAdapter;

    private RecyclerView recyclerView;
    private LinearLayout skletonPrincipale;
    private LinearLayout no_data_found,error_section;

    private LoginPreferencesManager loginPreferencesManager;
    private Retrofit2Client retrofit2Client;
    private NotificationApiService notificationApiService;
    private Long idUser;

    @SuppressLint("WrongViewCast")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        View toolbar = root.findViewById(R.id.toolbar_layout_notification);
        paddingStatusBar(toolbar);

        loginPreferencesManager = new LoginPreferencesManager(getContext());
        //user info
        User user = loginPreferencesManager.getUser();
        if (user != null) {
            idUser=user.getId();

        }

        retrofit2Client=new Retrofit2Client(getContext());
        notificationApiService = retrofit2Client.createService(NotificationApiService.class);

        recyclerView = root.findViewById(R.id.rv_notification);
        skletonPrincipale= root.findViewById(R.id.shimmerPrincipaleNot);
        error_section= root.findViewById(R.id.error_section_not);
        no_data_found= root.findViewById(R.id.ll_no_notification);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        notificationAdapter = new NotificationAdapter(getContext(),notifications);
        recyclerView.setAdapter(notificationAdapter);

        fetchNotifications(idUser,pageSize,currentPage);

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

    public int getStatusBarHeight() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = 0;
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }

        // Convert 10dp to pixels
        int extraHeight = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                10,
                Resources.getSystem().getDisplayMetrics()
        );

        return statusBarHeight + extraHeight;
    }
    private void paddingStatusBar(View layout){
        int statusBarHeight = getStatusBarHeight();
        layout.setPadding(layout.getPaddingLeft(), statusBarHeight, layout.getPaddingRight(), layout.getPaddingBottom());

    }

    private void fetchNotifications(Long userId,int perPage,int page) {
        isLoading = true;

        recyclerView.setVisibility(View.GONE);
        skletonPrincipale.setVisibility(View.VISIBLE);
        error_section.setVisibility(View.GONE);
        no_data_found.setVisibility(View.GONE);

        notificationApiService.getNotifications(userId,perPage,page).enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                isLoading = false;
                if (response.isSuccessful() && response.body() != null) {

                    if (!response.body().getData().isEmpty()){

                        recyclerView.setVisibility(View.VISIBLE);
                        skletonPrincipale.setVisibility(View.GONE);
                        error_section.setVisibility(View.GONE);
                        no_data_found.setVisibility(View.GONE);

                        totalPage = response.body().getPagination().getTotal_pages();
                        notifications.addAll(response.body().getData());
                        notificationAdapter.notifyDataSetChanged();

                    }
                    else{
                        recyclerView.setVisibility(View.GONE);
                        skletonPrincipale.setVisibility(View.GONE);
                        error_section.setVisibility(View.GONE);
                        no_data_found.setVisibility(View.VISIBLE);
                    }

                } else {

                    recyclerView.setVisibility(View.GONE);
                    skletonPrincipale.setVisibility(View.GONE);
                    error_section.setVisibility(View.VISIBLE);
                    no_data_found.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                isLoading = false;

                recyclerView.setVisibility(View.GONE);
                skletonPrincipale.setVisibility(View.GONE);
                error_section.setVisibility(View.VISIBLE);
                no_data_found.setVisibility(View.GONE);
            }
        });
    }

    private void loadMoreHotels() {
        isLoading = true;
        notificationAdapter.showLoading();
        new Handler().postDelayed(() -> {
            currentPage++;
            notificationApiService.getNotifications(idUser, pageSize,currentPage).enqueue(new Callback<NotificationResponse>() {
                @Override
                public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                    notificationAdapter.hideLoading();
                    isLoading = false;
                    if (response.isSuccessful() && response.body() != null) {
                        notificationAdapter.addHotels(response.body().getData());
                    }
                }

                @Override
                public void onFailure(Call<NotificationResponse> call, Throwable t) {
                    notificationAdapter.hideLoading();
                    isLoading = false;

                    recyclerView.setVisibility(View.GONE);
                    skletonPrincipale.setVisibility(View.GONE);
                    error_section.setVisibility(View.VISIBLE);
                    no_data_found.setVisibility(View.GONE);
                }
            });
        }, 2000);
    }


}