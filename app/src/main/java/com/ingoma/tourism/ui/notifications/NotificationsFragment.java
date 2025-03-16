package com.ingoma.tourism.ui.notifications;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ingoma.tourism.R;
import com.ingoma.tourism.adapter.NotificationAdapter;
import com.ingoma.tourism.databinding.FragmentNotificationsBinding;
import com.ingoma.tourism.model.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<Notification> notificationList;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        View toolbar = root.findViewById(R.id.toolbar_layout_notification);
        paddingStatusBar(toolbar);

        recyclerView = root.findViewById(R.id.rv_notification);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        notificationList = new ArrayList<>();
        notificationList.add(new Notification("New message received", "10:30 AM"));
        notificationList.add(new Notification("Your order has been shipped", "Yesterday"));
        notificationList.add(new Notification("Reminder: Meeting at 3 PM", "2 days ago"));
        notificationList.add(new Notification("New message received", "10:30 AM"));
        notificationList.add(new Notification("Your order has been shipped", "Yesterday"));
        notificationList.add(new Notification("Reminder: Meeting at 3 PM", "2 days ago"));
        notificationList.add(new Notification("New message received", "10:30 AM"));
        notificationList.add(new Notification("Your order has been shipped", "Yesterday"));
        notificationList.add(new Notification("Reminder: Meeting at 3 PM", "2 days ago"));

        adapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(adapter);

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


}