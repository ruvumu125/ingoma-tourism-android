package com.ingoma.tourism.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ingoma.tourism.BookingDetailsActivity;
import com.ingoma.tourism.model.HotelBooking;
import com.ingoma.tourism.R;
import java.util.List;

public class HotelBookingAdapter extends RecyclerView.Adapter<HotelBookingAdapter.ViewHolder> {
    private List<HotelBooking> bookings;
    private Context context;

    public HotelBookingAdapter(Context context,List<HotelBooking> bookings) {
        this.context = context;
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
        holder.booking_hotel_item.setOnClickListener(view -> {

            Intent intent = new Intent(context, BookingDetailsActivity.class);
            intent.putExtra("hotelName", booking.getHotelName());
            intent.putExtra("checkInDate", booking.getCheckInDate());
            intent.putExtra("checkOutDate", booking.getCheckOutDate());
            intent.putExtra("nights", booking.getNumberOfNights());
            intent.putExtra("guests", booking.getNumberOfGuests());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
       private AppCompatTextView tvHotelName, tv_booked_hotel_timing_info, tv_booked_hotel_pax_info;
       private ConstraintLayout booking_hotel_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHotelName = itemView.findViewById(R.id.tv_booked_hotel_name);
            tv_booked_hotel_timing_info = itemView.findViewById(R.id.tv_booked_hotel_timing_info);
            tv_booked_hotel_pax_info= itemView.findViewById(R.id.tv_booked_hotel_pax_info);
            booking_hotel_item=itemView.findViewById(R.id.booking_hotel_item);
        }
    }
}

