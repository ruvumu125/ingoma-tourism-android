package com.ingoma.tourism.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.ingoma.tourism.constant.Constant;
import com.ingoma.tourism.model.BookingList;
import com.ingoma.tourism.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BookingsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final List<BookingList> hotelList;
    private boolean isLoading;

    private static final int ITEM_VIEW_TYPE = 0;
    private static final int LOADING_VIEW_TYPE = 1;

    public BookingsListAdapter(Context context, List<BookingList> bookings) {
        this.context = context;
        this.hotelList = bookings;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == hotelList.size()) ? LOADING_VIEW_TYPE : ITEM_VIEW_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bookings_list, parent, false);
            return new HotelViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof HotelViewHolder) {
            BookingList booking = hotelList.get(position);
            HotelViewHolder hotelViewHolder = (HotelViewHolder) holder;

            String booking_duration="";
            if (booking.getPricingType().equals("daily")){
                if (Double.parseDouble(booking.getDuration())>1){
                    booking_duration="nuits";
                }
                else{
                    booking_duration="nuit";
                }
            }
            else{
                if (Double.parseDouble(booking.getDuration())>1){
                    booking_duration="mois";
                }
                else{
                    booking_duration="mois";
                }
            }

            String booking_status="";
            if (booking.getStatus().equals("pending")){

                booking_status="En attente";
                setBackgroundWithPadding(hotelViewHolder.tv_booked_hotel_pax_info, R.drawable.status_pending_bg);
            }
            else if (booking.getStatus().equals("confirmed")){

                booking_status="Confirmée";
                setBackgroundWithPadding(hotelViewHolder.tv_booked_hotel_pax_info, R.drawable.status_confirmed_bg);
            }
            else if (booking.getStatus().equals("cancelled")){
                booking_status="Annulée";
                setBackgroundWithPadding(hotelViewHolder.tv_booked_hotel_pax_info, R.drawable.status_cancelled_bg);
            }
            else{
                booking_status="Payée";
                setBackgroundWithPadding(hotelViewHolder.tv_booked_hotel_pax_info, R.drawable.status_paid_bg);
            }

            hotelViewHolder.tvHotelName.setText(booking.getProperty().getPropertyName());
            hotelViewHolder.tv_booking_start_date.setText(transformDateToFrenchFormat(booking.getCheckInDate()));
            hotelViewHolder.tv_booking_end_date.setText(transformDateToFrenchFormat(booking.getCheckOutDate()));
            hotelViewHolder.tv_booking_duration.setText(" | "+ removeDecimalIfZero(Double.parseDouble(booking.getDuration()))+" "+booking_duration);
            hotelViewHolder.tv_booked_hotel_pax_info.setText(booking_status );

            String baseUrl = Constant.BASE_URL + "api/v1/property-image/";
            String fullImageUrl=baseUrl + booking.getProperty().getPropertyMainImage();
            Glide.with(context)
                    .load(fullImageUrl) // Load first image
                    .placeholder(R.drawable.hotel_place_holder) // Add a placeholder image
                    .into(hotelViewHolder.iv_booked_hotel_img);

            hotelViewHolder.booking_hotel_item.setOnClickListener(view -> {

//                Intent intent = new Intent(context, BookingDetailsActivity.class);
//                intent.putExtra("hotelName", booking.getHotelName());
//                intent.putExtra("checkInDate", booking.getCheckInDate());
//                intent.putExtra("checkOutDate", booking.getCheckOutDate());
//                intent.putExtra("nights", booking.getNumberOfNights());
//                intent.putExtra("guests", booking.getNumberOfGuests());
//                context.startActivity(intent);
            });

        }
    }

    @Override
    public int getItemCount() {
        return isLoading ? hotelList.size() + 1 : hotelList.size();
    }
    public void addHotels(List<BookingList> newHotels) {
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

    public static class HotelViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView tvHotelName,tv_booking_start_date,tv_booking_end_date,tv_booking_duration, tv_booked_hotel_pax_info;
        private ConstraintLayout booking_hotel_item;
        private ShapeableImageView iv_booked_hotel_img;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);

            tvHotelName = itemView.findViewById(R.id.tv_booked_hotel_name);
            iv_booked_hotel_img = itemView.findViewById(R.id.iv_booked_hotel_img);
            tv_booking_start_date = itemView.findViewById(R.id.tv_booking_start_date);
            tv_booking_end_date = itemView.findViewById(R.id.tv_booking_end_date);
            tv_booking_duration = itemView.findViewById(R.id.tv_booking_duration);
            tv_booked_hotel_pax_info= itemView.findViewById(R.id.tv_booked_hotel_pax_info);
            booking_hotel_item=itemView.findViewById(R.id.booking_hotel_item);
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
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            Date date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Return null if parsing fails
        }
    }
    private static String removeDecimalIfZero(double number) {
        // Check if the number is a whole number (no value after the decimal point)
        if (number == (int) number) {
            return String.valueOf((int) number);  // Convert to integer and return as string
        } else {
            return String.valueOf(number);  // Return the original number as string
        }
    }

    public void setBackgroundWithPadding(AppCompatTextView textView, int drawableResId) {
        // Save the current padding
        int left = textView.getPaddingLeft();
        int top = textView.getPaddingTop();
        int right = textView.getPaddingRight();
        int bottom = textView.getPaddingBottom();

        // Set the new background
        textView.setBackgroundResource(drawableResId);

        // Restore the padding
        textView.setPadding(left, top, right, bottom);
    }


}

