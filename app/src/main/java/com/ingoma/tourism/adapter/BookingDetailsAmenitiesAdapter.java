package com.ingoma.tourism.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ingoma.tourism.R;
import com.ingoma.tourism.model.BookingDetailsRoomAmenity;
import com.ingoma.tourism.model.PropertyAmenity;

import java.util.List;

public class BookingDetailsAmenitiesAdapter extends RecyclerView.Adapter<BookingDetailsAmenitiesAdapter.AmenitiesViewHolder> {

    private List<BookingDetailsRoomAmenity> amenities;

    public BookingDetailsAmenitiesAdapter(List<BookingDetailsRoomAmenity> amenities) {
        this.amenities = amenities;
    }

    @NonNull
    @Override
    public AmenitiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_property_amenity, parent, false);
        return new AmenitiesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AmenitiesViewHolder holder, int position) {
        BookingDetailsRoomAmenity amenity = amenities.get(position);
        holder.amenityText.setText(amenity.getName());
        holder.amenityDescription.setText(amenity.getDescription());
    }

    @Override
    public int getItemCount() {
        return amenities.size();
    }

    static class AmenitiesViewHolder extends RecyclerView.ViewHolder {
        TextView amenityText,amenityDescription;

        public AmenitiesViewHolder(@NonNull View itemView) {
            super(itemView);
            amenityText = itemView.findViewById(R.id.amenityText);
            amenityDescription = itemView.findViewById(R.id.amenityDescription);
        }
    }
}
