package com.ingoma.tourism.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.ingoma.tourism.model.BookingList;
import com.ingoma.tourism.model.Notification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.ingoma.tourism.R;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final List<Notification> hotelList;
    private boolean isLoading;

    private static final int ITEM_VIEW_TYPE = 0;
    private static final int LOADING_VIEW_TYPE = 1;


    public NotificationAdapter(Context context, List<Notification> hotelList) {
        this.context = context;
        this.hotelList = hotelList;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == hotelList.size()) ? LOADING_VIEW_TYPE : ITEM_VIEW_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_VIEW_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_inbox, parent, false);
            return new NotificationViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof NotificationViewHolder) {

            Notification notification = hotelList.get(position);
            NotificationViewHolder hotelViewHolder = (NotificationViewHolder) holder;

            hotelViewHolder.tvMessage.setText(notification.getMessage());
            hotelViewHolder.tvTime.setText(transformDateToFrenchFormat(notification.getNotificationDate()));
        }

    }

    @Override
    public int getItemCount() {
        return isLoading ? hotelList.size() + 1 : hotelList.size();
    }
    public void addHotels(List<Notification> newHotels) {
        int startPosition = hotelList.size();
        hotelList.addAll(newHotels);
        notifyItemRangeInserted(startPosition, newHotels.size());
    }

    public void showLoading() {
        isLoading = true;
        notifyItemInserted(hotelList.size());
    }

    public void hideLoading() {
        isLoading = false;
        notifyItemRemoved(hotelList.size());
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tvMessage, tvTime;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_notification_title);
            tvTime = itemView.findViewById(R.id.tv_notification_time);
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
    // Function to clear data
    public void clearData() {
        hotelList.clear();
        notifyDataSetChanged();
    }
    private static String transformDateToFrenchFormat(String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        try {
            Date date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Return null if parsing fails
        }
    }
}

