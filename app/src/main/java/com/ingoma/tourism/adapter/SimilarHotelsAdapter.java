package com.ingoma.tourism.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ingoma.tourism.R;
import com.ingoma.tourism.model.Hotel;

import java.util.List;

public class SimilarHotelsAdapter extends RecyclerView.Adapter<SimilarHotelsAdapter.SimilarHotelViewHolder> {

    private Context context;
    private List<Hotel> similarHotels;

    public SimilarHotelsAdapter(Context context,List<Hotel> similarHotels) {
        this.context = context;
        this.similarHotels = similarHotels;
    }

    @NonNull
    @Override
    public SimilarHotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(context).inflate(R.layout.item_property_similar_property, parent, false);
        //return new SimilarHotelViewHolder(view);

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_property_similar_property, parent, false);
        return new SimilarHotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimilarHotelViewHolder holder, int position) {
        Hotel hotel = similarHotels.get(position);

        Glide.with(context)
                .load(hotel.getImageUrl()) // Load first image
                .placeholder(R.drawable.hotel_place_holder) // Add a placeholder image
                .into(holder.ivHotelImage);

        holder.tvHotelType.setText(hotel.getType());
        holder.tvHotelAddress.setText(hotel.getAddress());
        holder.tvHotelPrice.setText("$" + hotel.getPrice() + " / night");
        holder.ratingBar.setText(String.valueOf(hotel.getRating()));
    }

    @Override
    public int getItemCount() {
        return similarHotels.size();
    }

    public static class SimilarHotelViewHolder extends RecyclerView.ViewHolder {
        ImageView ivHotelImage;
        AppCompatTextView tvHoteName,tvHotelType,tvHotelAddress, ratingBar;
        TextView tvHotelPrice;

        public SimilarHotelViewHolder(@NonNull View itemView) {
            super(itemView);

            ivHotelImage = itemView.findViewById(R.id.img);
            tvHoteName = itemView.findViewById(R.id.tv_hotel_name);
            tvHotelType = itemView.findViewById(R.id.tv_hotel_type);
            tvHotelAddress = itemView.findViewById(R.id.tv_hotel_adress);
            tvHotelPrice = itemView.findViewById(R.id.prop_price);
            ratingBar = itemView.findViewById(R.id.rating_bar);
        }
    }
}

