package com.ingoma.tourism.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ingoma.tourism.model.HotelModel;
import com.smarteist.autoimageslider.SliderView;
import java.util.List;
import com.ingoma.tourism.R;

public class PropertyListAdapter extends RecyclerView.Adapter<PropertyListAdapter.HotelViewHolder> {

    private Context context;
    private List<HotelModel> hotelList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(HotelModel property);
    }

    public PropertyListAdapter(Context context, List<HotelModel> hotelList,OnItemClickListener onItemClickListener) {
        this.context = context;
        this.hotelList = hotelList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_property_list, parent, false);
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
        HotelModel hotel = hotelList.get(position);

        // Set hotel details
        holder.hotelName.setText(hotel.getName());
        holder.hotelType.setText(hotel.getType());
        holder.hotelAddress.setText(hotel.getAddress());
        holder.hotelPrice.setText("$" + hotel.getPrice());
        holder.hotelRating.setText("⭐ " + hotel.getRating());

        // Setup image slider
        SliderAdapter sliderAdapter = new SliderAdapter(context, hotel.getImageUrls(),() -> {
            onItemClickListener.onItemClick(hotel);
        });
        holder.imageSlider.setSliderAdapter(sliderAdapter);
        holder.imageSlider.setAutoCycle(true);
        holder.imageSlider.setScrollTimeInSec(3);

        // Setup amenities RecyclerView
        //holder.amenitiesRecyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false));
        //holder.amenitiesRecyclerView.setAdapter(new AmenitiesAdapter(hotel.getAmenities()));

        holder.main_card.setOnClickListener(v -> onItemClickListener.onItemClick(hotel));



    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    static class HotelViewHolder extends RecyclerView.ViewHolder {
        SliderView imageSlider;
        AppCompatTextView hotelType,hotelName,hotelAddress,hotelPrice,hotelRating;
        ConstraintLayout main_card;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            imageSlider = itemView.findViewById(R.id.view_pager);
            hotelName = itemView.findViewById(R.id.tv_hotel_name_yudita);
            hotelType = itemView.findViewById(R.id.tv_hotel_type);
            hotelAddress = itemView.findViewById(R.id.tv_hotel_address);
            hotelPrice = itemView.findViewById(R.id.tv_price);
            hotelRating = itemView.findViewById(R.id.rating_bar);
            main_card = itemView.findViewById(R.id.main_layout);
        }
    }
}
