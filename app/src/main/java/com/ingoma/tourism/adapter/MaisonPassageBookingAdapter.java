package com.ingoma.tourism.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.ingoma.tourism.R;
import com.ingoma.tourism.model.HotelBooking;

import java.util.List;

public class MaisonPassageBookingAdapter extends RecyclerView.Adapter<MaisonPassageBookingAdapter.ViewHolder> {

    private List<HotelBooking> bookings;

    public MaisonPassageBookingAdapter(List<HotelBooking> bookings) {
        this.bookings = bookings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bookings_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HotelBooking booking = bookings.get(position);
        holder.tvHotelName.setText(booking.getHotelName());
        holder.tv_booked_hotel_timing_info.setText(booking.getCheckInDate()+" - "+booking.getCheckOutDate()+" | "+booking.getNumberOfNights());
        holder.tv_booked_hotel_pax_info.setText("Visiteurs: "+booking.getNumberOfGuests());
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tvHotelName, tv_booked_hotel_timing_info, tv_booked_hotel_pax_info;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHotelName = itemView.findViewById(R.id.tv_booked_hotel_name);
            tv_booked_hotel_timing_info = itemView.findViewById(R.id.tv_booked_hotel_timing_info);
            tv_booked_hotel_pax_info= itemView.findViewById(R.id.tv_booked_hotel_pax_info);
        }
    }
}
