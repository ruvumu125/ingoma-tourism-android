package com.ingoma.tourism.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ingoma.tourism.R;
import com.ingoma.tourism.constant.Constant;
import com.ingoma.tourism.model.SimilarProperty;

import java.util.List;

public class SimilarPropertiesAdapter extends RecyclerView.Adapter<SimilarPropertiesAdapter.SimilarHotelViewHolder> {

    private Context context;
    private List<SimilarProperty> similarHotels;

    public SimilarPropertiesAdapter(Context context, List<SimilarProperty> similarHotels) {
        this.context = context;
        this.similarHotels = similarHotels;
    }

    @NonNull
    @Override
    public SimilarHotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_property_similar_property, parent, false);
        return new SimilarHotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimilarHotelViewHolder holder, int position) {
        SimilarProperty hotel = similarHotels.get(position);

        String baseUrl = Constant.BASE_URL + "api/v1/property-image/";
        String fullImageUrl = baseUrl+ hotel.getFirstImage();

        Glide.with(context)
                .load(fullImageUrl) // Load first image
                .placeholder(R.drawable.hotel_place_holder) // Add a placeholder image
                .into(holder.ivHotelImage);

        holder.tvHotelType.setText(hotel.getPropertyType());
        holder.tvHotelAddress.setText(hotel.getCity().getName());
        holder.tvHotelPrice.setText(hotel.getMinPrice());
        holder.tvCurrency.setText(hotel.getCurrency());
        //holder.ratingBar.setText(String.valueOf(hotel.getRating()));
    }

    @Override
    public int getItemCount() {
        return similarHotels.size();
    }

    public static class SimilarHotelViewHolder extends RecyclerView.ViewHolder {
        ImageView ivHotelImage;
        AppCompatTextView tvHoteName,tvHotelType,tvHotelAddress, ratingBar;
        TextView tvHotelPrice,tvCurrency;

        public SimilarHotelViewHolder(@NonNull View itemView) {
            super(itemView);

            ivHotelImage = itemView.findViewById(R.id.img);
            tvHoteName = itemView.findViewById(R.id.tv_hotel_name);
            tvHotelType = itemView.findViewById(R.id.tv_hotel_type);
            tvHotelAddress = itemView.findViewById(R.id.tv_hotel_adress);
            tvHotelPrice = itemView.findViewById(R.id.prop_price);
            tvCurrency = itemView.findViewById(R.id.taxes);
            ratingBar = itemView.findViewById(R.id.rating_bar);
        }
    }
}

